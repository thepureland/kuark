package io.kuark.ability.workflow.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.kuark.ability.workflow.definition.FlowDefinition
import io.kuark.base.error.ObjectAlreadyExistsException
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.string.appendIfMissing
import io.kuark.base.log.LogFactory
import org.activiti.bpmn.converter.BpmnXMLConverter
import org.activiti.editor.language.json.converter.BpmnJsonConverter
import org.activiti.engine.ActivitiIllegalArgumentException
import org.activiti.engine.RepositoryService
import org.activiti.engine.repository.Model
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
 * 流程模型相关业务
 *
 * @author K
 * @since 1.0.0
 */
open class FlowModelBiz : IFlowModelBiz {

    @Autowired
    private lateinit var repositoryService: RepositoryService

    private val log = LogFactory.getLog(this::class)

    override fun isExists(key: String, version: Int?): Boolean {
        require(key.isNotBlank()) { "检测流程模型是否存在失败！【key】参数不能为空！" }
        val query = repositoryService.createModelQuery().modelKey(key)
        if (version != null) {
            query.modelVersion(version)
        }
        return query.count() > 0
    }

    override fun get(key: String, version: Int?): FlowModel? {
        require(key.isNotBlank()) { "获取流程模型失败！【key】参数不能为空！" }
        val model = getActivitiModel(key, version) ?: return null
        return FlowModel(model)
    }

    override fun search(criteria: FlowModelCriteria, pageNum: Int, limit: Int): List<FlowModel> {
        val whereStr = StringBuilder("1=1")

        // 流程key(bpmn文件中process元素的id)
        val key = criteria.key
        if (key != null && key.isNotBlank() && !key.contains("'")) {
            whereStr.append(" AND UPPER(key_) LIKE '%${key.uppercase()}%'")
        }

        // 流程名称，支持忽略大小写模糊搜索
        val name = criteria.name
        if (name != null && name.isNotBlank() && !name.contains("'")) {
            whereStr.append(" AND UPPER(name_) LIKE '%${name.uppercase()}%'")
        }

        // 分类
        val category = criteria.category
        if (category != null && category.isNotBlank() && !category.contains("'")) {
            whereStr.append(" AND category_ = '${category}'")
        }

        // 租户(所属系统)id
        val tenantId = criteria.tenantId
        if (tenantId != null && tenantId.isNotBlank() && !tenantId.contains("'")) {
            whereStr.append(" AND tenant_id_ = '${tenantId}'")
        }

        // 只查询最新版本的
        val latestOnly = criteria.latestOnly
        var sql = "SELECT * FROM act_re_model WHERE $whereStr"
        if (latestOnly) {
            sql = "$sql AND version_ = (SELECT MAX(version_) FROM act_re_model GROUP BY key_)"
        }

        // 查询
        val query = repositoryService.createNativeModelQuery().sql(sql)
        val models = if (limit < 1) {
            query.list()
        } else {
            val pageNo = if (pageNum < 1) 1 else pageNum
            query.listPage((pageNo - 1) * limit, limit)
        }

        // 结果处理
        return models.map { FlowModel(it) }
    }

    @Transactional
    override fun create(
        key: String, name: String, category: String, flowJson: String, svgXml: String, tenantId: String?
    ): FlowModel {
        require(key.isNotBlank()) { "创建流程模型失败！【key】参数不能为空！" }
        require(name.isNotBlank()) { "创建流程模型失败！【name】参数不能为空！" }
        require(flowJson.isNotBlank()) { "创建流程模型失败！【flowJson】参数不能为空！" }
        require(svgXml.isNotBlank()) { "创建流程模型失败！【svgXml】参数不能为空！" }
        require(!key.contains("'")) { "创建流程模型失败！【key】参数不能包含单引号！" }
        require(!name.contains("'")) { "创建流程模型失败！【name】参数不能包含单引号！" }
        require(!category.contains("'")) { "创建流程模型失败！【category】参数不能包含单引号！" }
        if (tenantId != null) {
            require(!tenantId.contains("'")) { "创建流程模型失败！【tenantId】参数不能包含单引号！" }
        }

        if (isExists(key)) {
            throw ObjectAlreadyExistsException("创建流程模型失败！【key：${key}】对应的流程模型已经存在！")
        }
        val model = repositoryService.newModel()
        setModelProperties(model, key, name, category, tenantId)
        repositoryService.saveModel(model)

        // 保存流程内容
        repositoryService.addModelEditorSource(model.id, flowJson.toByteArray(charset("utf-8")))

        // 保存流程图
        repositoryService.addModelEditorSourceExtra(model.id, svgToByteArray(svgXml))

        log.info("创建流程模型成功！【key：$key，name：$name】")
        return FlowModel(model)
    }

    @Transactional
    override fun update(
        key: String,
        version: Int?,
        name: String?,
        category: String?,
        svgXml: String?,
        flowJson: String?,
        tenantId: String?
    ): FlowModel {
        require(key.isNotBlank()) { "更新流程模型失败！【流程key】参数不能为空！" }
        if (name != null) {
            require(!name.contains("'")) { "更新流程模型失败！【流程名称】参数不能包含单引号！" }
        }
        var model = getActivitiModel(key, version)
        model ?: throw ObjectNotFoundException("更新流程模型失败！【key：${key}】对应的流程模型不存在！")

        if (model.deploymentId != null && model.deploymentId.isNotBlank()) {
            // 模型已部署，新增一条最新版本的模型记录
            val latestVersion = if (version == null) {
                model.version
            } else {
                val activitiModel = getActivitiModel(key)!!
                activitiModel.version
            }
            model = repositoryService.newModel()
            model.version = latestVersion + 1
            log.info("更新流程模型【key：$key，version：$version】时发现其已部署，新增一条最新版本的模型记录【key：$key，version：${model.version}】。")
        }
        setModelProperties(model!!, key, name, category, tenantId)
        repositoryService.saveModel(model) // 新增或更新

        // 保存流程内容
        if (flowJson != null && flowJson.isNotBlank()) {
            repositoryService.addModelEditorSource(model.id, flowJson.toByteArray(charset("utf-8")))
        }

        // 保存流程图
        if (svgXml != null && svgXml.isNotBlank()) {
            repositoryService.addModelEditorSourceExtra(model.id, svgToByteArray(svgXml))
        }

        log.info("更新流程模型成功！【key：$key，version：$version】")
        return FlowModel(model)
    }

    @Transactional
    override fun deploy(key: String, version: Int?): FlowDefinition {
        require(key.isNotBlank()) { "部署流程失败！【流程key】不能为空！" }

        // 获取流程模型
        val model = getActivitiModel(key, version)
        model ?: throw ObjectNotFoundException("部署流程失败！【key：${key}】对应的流程模型不存在！")

        // 检测该版本的流程模型是否已经部署过
        val deployments = repositoryService.createDeploymentQuery().processDefinitionKey(key).list()
        val exists = deployments.any { it.version == model.version }
        if (exists) {
            throw IllegalStateException("部署流程失败！流程模型已经部署过，不能重复部署！【key：$key，version：$version】")
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
            .name(name)
            .addString("${name}.bpmn20.xml", String(bpmnBytes))
            .addBytes("${name}.png", bytes)
            .deploy()

        // 设置模型的部署id
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
            if (diagramFileName != null && diagramFileName.isNotEmpty()) {
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

    private fun getActivitiModel(key: String, version: Int? = null): Model? {
        require(key.isNotBlank()) { "获取流程模型时，key参数不能为空！" }
        val query = repositoryService.createModelQuery().modelKey(key)
        if (version == null) {
            query.latestVersion()
        } else {
            query.modelVersion(version)
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
            if (name != null && name.isNotBlank()) { // 为空忽略更新
                this.name = name
            }
            if (category != null && category.isNotBlank()) { // 为空忽略更新
                this.category = category
            }
            if (tenantId != null && tenantId.isNotBlank()) { // 为空忽略更新
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