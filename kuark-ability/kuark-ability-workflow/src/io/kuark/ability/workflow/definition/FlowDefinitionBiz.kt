package io.kuark.ability.workflow.definition

import io.kuark.ability.workflow.event.IFlowEventListener
import io.kuark.ability.workflow.instance.FlowInstance
import io.kuark.base.error.ObjectAlreadyExistsException
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.collections.joinEachToString
import io.kuark.base.log.LogFactory
import org.activiti.engine.ActivitiException
import org.activiti.engine.ActivitiObjectNotFoundException
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.repository.Deployment
import org.activiti.engine.repository.ProcessDefinition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.lang.StringBuilder

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

        val query = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
        if (version != null) {
            query.processDefinitionVersion(version)
        }
        return query.count() > 0
    }

    override fun get(key: String, version: Int?): FlowDefinition? {
        require(key.isNotBlank()) { "获取流程定义失败！【key】参数不能为空！" }

        val def = getActivitiDefinition(key, version)
        return if (def == null) {
            log.warn("找不到流程定义！key：$key")
            null
        } else {
            val deployment = repositoryService.createDeploymentQuery().deploymentId(def.deploymentId).singleResult()
            FlowDefinition(def, deployment)
        }
    }

    override fun search(criteria: FlowDefinitionCriteria, pageNum: Int, limit: Int): List<FlowDefinition> {
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
        var sql = "SELECT * FROM act_re_procdef WHERE $whereStr"
        if (latestOnly) {
            sql = "$sql AND version_ = (SELECT MAX(def.version_) FROM act_re_procdef def GROUP BY def.key_)"
        }

        // 查询流程定义
        val query = repositoryService.createNativeProcessDefinitionQuery().sql(sql)
        val definitions = if (limit < 1) {
            query.list()
        } else {
            val pageNo = if (pageNum < 1) 1 else pageNum
            query.listPage((pageNo - 1) * limit, limit)
        }

        // 查询流程部署
        val deploymentIds = definitions.map { it.deploymentId }
        val deploymentIdsStr = deploymentIds.joinEachToString("'", "'", ",")
        sql = "SELECT * FROM act_re_deployment WHERE id_ IN (${deploymentIdsStr})"
        val deployments = repositoryService.createNativeDeploymentQuery().sql(sql).list()

        // 结果处理
        val deploymentMap = mutableMapOf<String, Deployment>()
        deployments.forEach { deploymentMap[it.id] = it }
        return definitions.map { FlowDefinition(it, deploymentMap[it.deploymentId]) }
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


    private fun getActivitiDefinition(key: String, version: Int?): ProcessDefinition? {
        val query = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
        if (version == null) {
            query.latestVersion()
        } else {
            query.processDefinitionVersion(version)
        }
        return query.singleResult()
    }

}