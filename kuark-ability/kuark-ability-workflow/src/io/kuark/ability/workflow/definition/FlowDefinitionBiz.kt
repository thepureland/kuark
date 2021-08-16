package io.kuark.ability.workflow.definition

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.ability.workflow.event.IFlowEventListener
import io.kuark.ability.workflow.instance.FlowInstance
import io.kuark.base.error.ObjectAlreadyExistsException
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.collections.joinEachToString
import io.kuark.base.lang.string.StringKit
import io.kuark.base.lang.string.appendIfMissing
import io.kuark.base.log.LogFactory
import io.kuark.base.query.sort.Order
import org.activiti.bpmn.converter.BpmnXMLConverter
import org.activiti.editor.language.json.converter.BpmnJsonConverter
import org.activiti.engine.*
import org.activiti.engine.repository.Deployment
import org.activiti.engine.repository.Model
import org.activiti.engine.repository.ProcessDefinition
import org.activiti.image.impl.DefaultProcessDiagramGenerator
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.io.*
import java.util.zip.ZipInputStream
import javax.xml.stream.XMLInputFactory

/**
 * 流程定义相关业务
 *
 * 每部署一次，都会生成一个新的流程定义
 *
 * @author K
 * @since 1.0.0
 */
open class FlowDefinitionBiz : IFlowDefinitionBiz {

    @Autowired
    private lateinit var repositoryService: RepositoryService

    @Autowired
    private lateinit var runtimeService: RuntimeService

    private val log = LogFactory.getLog(this::class)


    override fun isExists(key: String, version: Int?): Boolean {
        require(key.isNotBlank()) { "检测流程定义是否存在失败！【key】参数不能为空！" }

        val query = repositoryService.createModelQuery().modelKey(key)
        if (version != null) {
            query.modelVersion(version)
        }
        return query.count() > 0
    }

    override fun get(key: String, version: Int?): FlowDefinition? {
        require(key.isNotBlank()) { "获取流程定义失败！【key】参数不能为空！" }

        val model = getActivitiModel(key, version)
        return if (model == null) {
            log.warn("找不到流程定义！key：$key")
            null
        } else {
            val def = getActivitiDefinition(key, version)
            if (def == null) {
                FlowDefinition(model)
            } else {
                val deployment = repositoryService.createDeploymentQuery().deploymentId(def.deploymentId).singleResult()
                FlowDefinition(def, deployment)
            }
        }
    }

    override fun search(
        queryItems: FlowDefinitionQueryItems,
        pageNum: Int,
        pageSize: Int,
        vararg orders: Order
    ): List<FlowDefinition> {
        val whereStr = StringBuilder("1=1")

        // 流程key(bpmn文件中process元素的id)
        val key = queryItems.key
        if (StringKit.isNotBlank(key) && !key!!.contains("'")) {
            whereStr.append(" AND UPPER(key_) LIKE '%${key.uppercase()}%'")
        }

        // 流程名称，支持忽略大小写模糊搜索
        val name = queryItems.name
        if (StringKit.isNotBlank(name) && !name!!.contains("'")) {
            whereStr.append(" AND UPPER(name_) LIKE '%${name.uppercase()}%'")
        }

        // 分类
        val category = queryItems.category
        if (StringKit.isNotBlank(category) && !category!!.contains("'")) {
            whereStr.append(" AND category_ = '${category}'")
        }

        // 租户(所属系统)id
        val tenantId = queryItems.tenantId
        if (StringKit.isNotBlank(tenantId) && !tenantId!!.contains("'")) {
            whereStr.append(" AND tenant_id_ = '${tenantId}'")
        }

        // 是否已部署
        val isDeployed = queryItems.isDeployed
        if (isDeployed != null) {
            if (isDeployed) {
                whereStr.append(" AND deployment_id_ IS NOT NULL")
            } else {
                whereStr.append(" AND deployment_id_ IS NULL")
            }
        }

        // 排序
        val orderStr = RdbKit.getOrderSql(*orders)

        // 只查询最新版本的
        val latestOnly = queryItems.latestOnly
        var sql = "SELECT * FROM act_re_model WHERE $whereStr"
        if (latestOnly) {
            sql = "$sql AND version_ = (SELECT MAX(m.version_) FROM act_re_model m GROUP BY m.key_)"
        }
        sql = "$sql $orderStr"

        // 查询流程定义
        val query = repositoryService.createNativeModelQuery().sql(sql)
        val models = if (pageSize < 1) {
            query.list()
        } else {
            val pageNo = if (pageNum < 1) 1 else pageNum
            query.listPage((pageNo - 1) * pageSize, pageSize)
        }

        // 查询流程部署
        val deploymentIds = models.map { it.deploymentId }
        val deploymentIdsStr = deploymentIds.joinEachToString("'", "'", ",")
        sql = "SELECT * FROM act_re_deployment WHERE id_ IN (${deploymentIdsStr})"
        val deployments = repositoryService.createNativeDeploymentQuery().sql(sql).list()

        // 结果处理
        val deploymentMap = mutableMapOf<String, Deployment>()
        deployments.forEach { deploymentMap[it.id] = it }
        return models.map { FlowDefinition(it, deploymentMap[it.deploymentId]) }
    }

    @Transactional
    override fun create(
        key: String, name: String, category: String, flowJson: String, svgXml: String, tenantId: String?
    ): FlowDefinition {
        require(key.isNotBlank()) { "创建流程定义失败！【key】参数不能为空！" }
        require(name.isNotBlank()) { "创建流程定义失败！【name】参数不能为空！" }
        require(flowJson.isNotBlank()) { "创建流程定义失败！【flowJson】参数不能为空！" }
        require(svgXml.isNotBlank()) { "创建流程定义失败！【svgXml】参数不能为空！" }
        require(!key.contains("'")) { "创建流程定义失败！【key】参数不能包含单引号！" }
        require(!name.contains("'")) { "创建流程定义失败！【name】参数不能包含单引号！" }
        require(!category.contains("'")) { "创建流程定义失败！【category】参数不能包含单引号！" }
        if (tenantId != null) {
            require(!tenantId.contains("'")) { "创建流程定义失败！【tenantId】参数不能包含单引号！" }
        }

        if (isExists(key)) {
            throw ObjectAlreadyExistsException("创建流程定义失败！【key：${key}】对应的流程定义已经存在！")
        }
        val model = repositoryService.newModel()
        setModelProperties(model, key, name, category, tenantId)
        repositoryService.saveModel(model)

        // 保存流程内容
        repositoryService.addModelEditorSource(model.id, flowJson.toByteArray(charset("utf-8")))

        // 保存流程图
        repositoryService.addModelEditorSourceExtra(model.id, svgToByteArray(svgXml))

        log.info("创建流程定义成功！【key：$key，name：$name】")
        return FlowDefinition(model)
    }

    @Transactional
    override fun update(
        key: String,
        version: Int?,
        name: String?,
        category: String?,
        flowJson: String?,
        svgXml: String?,
        tenantId: String?
    ): FlowDefinition {
        require(key.isNotBlank()) { "更新流程定义失败！【流程key】参数不能为空！" }
        if (name != null) {
            require(!name.contains("'")) { "更新流程定义失败！【流程名称】参数不能包含单引号！" }
        }
        var model = getActivitiModel(key, version)
        model ?: throw ObjectNotFoundException("更新流程定义失败！【key：${key}】对应的流程定义不存在！")

        if (StringKit.isNotBlank(model.deploymentId)) {
            // 流程已部署，新增一条最新版本的流程定义记录
            val latestVersion = if (version == null) {
                model.version
            } else {
                val activitiModel = getActivitiModel(key)!!
                activitiModel.version
            }
            model = repositoryService.newModel()
            model.version = latestVersion + 1
            log.info("更新流程定义【key：$key，version：$latestVersion】时发现其已部署，新增一条最新版本的流程定义记录【key：$key，version：${model.version}】。")
        }
        setModelProperties(model!!, key, name, category, tenantId)
        repositoryService.saveModel(model) // 新增或更新

        // 保存流程内容
        if (StringKit.isNotBlank(flowJson)) {
            repositoryService.addModelEditorSource(model.id, flowJson!!.toByteArray(charset("utf-8")))
        }

        // 保存流程图
        if (StringKit.isNotBlank(svgXml)) {
            repositoryService.addModelEditorSourceExtra(model.id, svgToByteArray(svgXml!!))
        }

        log.info("更新流程定义成功！【key：$key，version：$version】")
        return FlowDefinition(model)
    }

    @Transactional
    override fun deploy(key: String, version: Int?): FlowDefinition {
        require(key.isNotBlank()) { "部署流程失败！【流程key】不能为空！" }

        // 获取流程定义
        val model = getActivitiModel(key, version)
        model ?: throw ObjectNotFoundException("部署流程失败，流程定义不存在！【key：$key，version：$version】")

        // 检测该版本的流程定义是否已经部署过
        val deployments = repositoryService.createDeploymentQuery().processDefinitionKey(key).list()
        val exists = deployments.any { it.version == model.version }
        if (exists) {
            throw IllegalStateException("部署流程失败！流程定义已经部署过，不能重复部署！【key：$key，version：$version】")
        }

        // 流程内容
        val modelEditorSource = repositoryService.getModelEditorSource(model.id)
        val modelNode = ObjectMapper().readTree(modelEditorSource) as ObjectNode
        val bpmnModel = BpmnJsonConverter().convertToBpmnModel(modelNode)
        val definition = bpmnModel.processes.first()
        definition.id = key // 设置流程key(bpmn文件中process元素的id)
        definition.name = model.name
        val bpmnBytes = BpmnXMLConverter().convertToXML(bpmnModel)

        // 流程图
        val bytes = repositoryService.getModelEditorSourceExtra(model.id)

        // 部署
        val name = model.name
        val deployment = repositoryService.createDeployment()
            .key(key)
            .category(model.category)
            .tenantId(model.tenantId)
            .name(name)
            .addString("${name}.bpmn20.xml", String(bpmnBytes))
            .addBytes("${name}.png", bytes)
            .deploy()

        // 设置定义的部署id
        model.deploymentId = deployment.id
        repositoryService.saveModel(model)

        log.info("部署流程成功！【key：$key，version：$version】")
        return FlowDefinition(deployment, model.id)
    }

    @Transactional
    override fun deployWithBpmn(
        name: String, bpmnFileName: String, diagramFileName: String?, prefixPath: String
    ): FlowDefinition {
        val bpmnFilePath = "${prefixPath}/${bpmnFileName}".appendIfMissing(".bpmn")
        val deploymentBuilder = repositoryService.createDeployment().name(name)
        try {
            deploymentBuilder.addClasspathResource(bpmnFilePath)
            if (StringKit.isNotBlank(diagramFileName)) {
                val diagramFilePath = "${prefixPath}/${diagramFileName}".appendIfMissing(".png")
                deploymentBuilder.addClasspathResource(diagramFilePath)
            }
        } catch (e: ActivitiIllegalArgumentException) {
            log.error(e)
            throw ObjectNotFoundException(e)
        }
        val deployment = deploymentBuilder.deploy()
        val definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.id).singleResult()
        return FlowDefinition(definition, deployment)
    }

    @Transactional
    override fun deployWithZip(deploymentName: String, zipFileName: String, prefixPath: String): List<FlowDefinition> {
        val zipPath = "${prefixPath}/${zipFileName}".appendIfMissing(".zip")
        val zipFile = File(zipPath)
        val inputStream = try {
            FileInputStream(zipFile)
        } catch (e: FileNotFoundException) {
            log.error(e)
            throw ObjectNotFoundException(e)
        }
        val zipInputStream = ZipInputStream(inputStream)
        val deployment = zipInputStream.use {
            repositoryService.createDeployment().name(deploymentName).addZipInputStream(it).deploy()
        }
        val definitions = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.id).list()
        return definitions.map { FlowDefinition(it, deployment) }.toList()
    }

    override fun getFlowDiagram(key: String, version: Int?): InputStream? {
        val query = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
        if (version == null) {
            query.latestVersion()
        } else {
            query.processDefinitionVersion(version)
        }
        val definition = query.singleResult()

        return if (definition != null) {
            repositoryService.getResourceAsStream(definition.deploymentId, definition.diagramResourceName)
        } else {
            log.error("流程图获取失败，因找不到流程定义！key：$key，可能该流程尚未部署！")
            null
        }
    }

    override fun genFlowDiagram(bpmnFileName: String, prefixPath: String): InputStream {
        val filePath = "${prefixPath}/${bpmnFileName}".appendIfMissing(".bpmn")
        val bpmnFile = File(filePath)
        return if (bpmnFile.exists()) {
            val inputStream = FileInputStream(bpmnFile)
            val bpmnModel = inputStream.use {
                val streamReader = XMLInputFactory.newInstance().createXMLStreamReader(it)
                BpmnXMLConverter().convertToBpmnModel(streamReader)
            }
            // generateDiagram(bpmnModel, "png",  "宋体", "微软雅黑", "黑体", null, 2.0)
            DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, emptyList())
        } else {
            val errMsg = "生成流程图失败，因bpmn文件【${filePath}】不存在！"
            log.error(errMsg)
            throw ObjectNotFoundException(errMsg)
        }
    }

    @Transactional
    override fun activate(key: String, version: Int?) {
        require(key.isNotBlank()) { "激活流程定义失败！【key】参数不能为空！" }

        val def = getActivitiDefinition(key, version)
        def ?: throw ObjectNotFoundException("找不到流程定义！【key：$key，version：$version】")
        try {
            repositoryService.activateProcessDefinitionById(def.id)
            log.info("激活流程定义成功！【key：$key，version：$version】")
        } catch (e: ActivitiObjectNotFoundException) {
            log.error(e)
        } catch (e: ActivitiException) {
            if (e.message!!.contains("already in state")) {
                log.warn("忽略流程定义激活操作，因其已处于激活状态！【key: $key】")
            } else {
                log.error(e)
            }
        }
    }

    @Transactional
    override fun suspend(key: String, version: Int?) {
        require(key.isNotBlank()) { "挂起流程定义失败！【key】参数不能为空！" }

        try {
            repositoryService.suspendProcessDefinitionByKey(key)
            log.info("挂起流程定义成功！【key：$key，version：$version】")
        } catch (e: ActivitiObjectNotFoundException) {
            log.error(e)
            throw IllegalArgumentException(e)
        } catch (e: ActivitiException) {
            log.warn("忽略流程定义挂起操作，因其已处于挂起状态！【key: $key】")
        }
    }

    @Transactional
    override fun delete(key: String, version: Int?, cascade: Boolean) {
        require(key.isNotBlank()) { "删除流程定义失败！【key】参数不能为空！" }

        val definition = get(key)
        if (definition == null) {
            val errMsg = "找不到流程定义！【key：$key】"
            log.error(errMsg)
            throw ObjectNotFoundException(errMsg)
        }
        try {
            repositoryService.deleteDeployment(definition._deploymentId, cascade)
        } catch (e: ActivitiObjectNotFoundException) {
            log.error(e)
            throw ObjectNotFoundException(e)
        }
    }

    @Transactional
    override fun start(
        key: String,
        bizKey: String,
        instanceName: String,
        variables: Map<String, *>?,
        eventListener: IFlowEventListener?,
        version: Int?
    ): FlowInstance? {
        // 参数处理
        require(key.isNotBlank()) { "启动流程失败！【key】参数不能为空！【bizKey: $bizKey】" }
        require(instanceName.isNotBlank()) { "启动流程失败！【instanceName】参数不能为空！【key：$key，bizKey: $bizKey】" }
        require(bizKey.isNotBlank()) { "启动流程失败！【bizKey】参数不能为空！【key：$key】" }
        require(!bizKey.contains("'")) { "启动流程失败！指定的业务主键不能包含单引号！【key：$key，bizKey: $bizKey】" }
        require(!instanceName.contains("'")) { "启动流程失败！指定的实例名称不能包含单引号！【key：$key，bizKey: $bizKey】" }

        // 检测流程实例是否已经存在
        val query = runtimeService.createProcessInstanceQuery()
            .processDefinitionKey(key)
            .processInstanceBusinessKey(bizKey)
        if (version == null) {
            val definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(key).latestVersion().singleResult()
            if (definition == null) {
                val errMsg = "启动流程失败，因流程定义不存在！【key：$key，version：$version，bizKey：$bizKey】"
                log.error(errMsg)
                throw ObjectNotFoundException(errMsg)
            }
            query.processDefinitionVersion(definition.version)
        } else {
            query.processDefinitionVersion(version)
        }
        if (query.count() > 0) {
            val errMsg = "启动流程失败，因流程实例已存在！【key：$key，version：$version，bizKey：$bizKey】"
            log.error(errMsg)
            throw ObjectAlreadyExistsException(errMsg)
        }

        val instance = try {
            // 添加事件监听器
            if (eventListener != null) {
                runtimeService.addEventListener(eventListener)
            }

            // 启动流程
            runtimeService.startProcessInstanceByKey(key, bizKey, variables).apply {
                log.info("启动流程成功！【key：$key，version：$version，bizKey：$bizKey】")
            }
        } catch (e: ActivitiObjectNotFoundException) {
            val errMsg = "启动流程失败，因流程定义不存在！【key：$key，version：$version，bizKey：$bizKey】"
            log.error(errMsg)
            throw ObjectNotFoundException(errMsg)
        } catch (e: Throwable) {
            log.error(e, "启动流程失败！【key: $key，version：$version, bizKey: $bizKey】")
            null
        }

        return if (instance == null) {
            null
        } else {
            // 设置流程实例名称
            runtimeService.setProcessInstanceName(instance.processInstanceId, instanceName)
            log.info("启动流程后设置流程实例名称成功！【instanceName：$instanceName，key：$key，version：$version，bizKey：$bizKey】")
            FlowInstance(instance).apply {
                this.name = instanceName
            }
        }
    }

    private fun getActivitiModel(key: String, version: Int? = null): Model? {
        val query = repositoryService.createModelQuery().modelKey(key)
        if (version == null) {
            query.latestVersion()
        } else {
            query.modelVersion(version)
        }
        return query.singleResult()
    }

    private fun getActivitiDefinition(key: String, version: Int?): ProcessDefinition? {
        val query = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
        if (version == null) {
            query.latestVersion()
        } else {
            query.processDefinitionVersion(version)
        }
        return query.singleResult()
    }

    private fun setModelProperties(
        model: Model, key: String, name: String?, category: String?, tenantId: String?
    ) {
//        val modelObjectNode = if (model.id == null) {
//            ObjectMapper().createObjectNode()
//        } else {
//            ObjectMapper().readTree(model.metaInfo) as ObjectNode
//        }
//        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name)
//        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1)
        model.apply {
            this.key = key
            if (StringKit.isNotBlank(name)) { // 为空忽略更新
                this.name = name
            }
            if (StringKit.isNotBlank(category)) { // 为空忽略更新
                this.category = category
            }
            if (StringKit.isNotBlank(tenantId)) { // 为空忽略更新
                this.tenantId = tenantId
            }
//            this.metaInfo = modelObjectNode.toString()
        }
    }

    private fun svgToByteArray(svgXml: String): ByteArray {
        val svgStream = ByteArrayInputStream(svgXml.toByteArray(charset("utf-8")))
        val input = TranscoderInput(svgStream)
        val transcoder = PNGTranscoder()
        val outStream = ByteArrayOutputStream()
        val output = TranscoderOutput(outStream)
        transcoder.transcode(input, output)
        val result = outStream.toByteArray()
        outStream.close()
        svgStream.close()
        return result
    }

}