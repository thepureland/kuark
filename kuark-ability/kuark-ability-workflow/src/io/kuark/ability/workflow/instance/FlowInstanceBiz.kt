package io.kuark.ability.workflow.instance

import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import org.activiti.engine.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.lang.StringBuilder

/**
 * 流程实例相关业务
 *
 * @author K
 * @since 1.0.0
 */
open class FlowInstanceBiz : IFlowInstanceBiz {

    @Autowired
    private lateinit var runtimeService: RuntimeService

    @Autowired
    private lateinit var repositoryService: RepositoryService

    private val log = LogFactory.getLog(this::class)

    override fun isExists(key: String, bizKey: String, version: Int?): Boolean {
        val query = runtimeService.createProcessInstanceQuery()
            .processDefinitionKey(key)
            .processInstanceBusinessKey(bizKey)
        if (version == null) {
            val definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(key).latestVersion().singleResult()
            definition ?: return false
            query.processDefinitionVersion(definition.version)
        } else {
            query.processDefinitionVersion(version)
        }
        return query.count() > 0
    }

    override fun get(key: String, bizKey: String, version: Int?): FlowInstance? {
        val query = runtimeService.createProcessInstanceQuery()
            .processDefinitionKey(key)
            .processInstanceBusinessKey(bizKey)
        if (version == null) {
            val definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(key).latestVersion().singleResult()
            definition ?: return null
            query.processDefinitionVersion(definition.version)
        } else {
            query.processDefinitionVersion(version)
        }

        val instance = query.singleResult()
        return if (instance == null) null else FlowInstance(instance)
    }

    override fun search(queryItems: FlowInstanceQueryItems, pageNum: Int, limit: Int): List<FlowInstance> {
        val whereStr = StringBuilder("e.parent_id_ IS NULL")

        // 流程key(bpmn文件中process元素的id)
        val key = queryItems.key
        if (StringKit.isNotBlank(key) && !key!!.contains("'")) {
            whereStr.append(" AND UPPER(d.key_) LIKE '%${key.uppercase()}%'")
        }

        // 业务主键
        val bizKey = queryItems.bizKey
        if (StringKit.isNotBlank(bizKey) && !bizKey!!.contains("'")) {
            whereStr.append(" AND UPPER(e.business_key_) LIKE '%${bizKey.uppercase()}%'")
        }

        // 实例名称
        val instanceName = queryItems.instanceName
        if (StringKit.isNotBlank(instanceName) && !instanceName!!.contains("'")) {
            whereStr.append(" AND UPPER(e.name_) LIKE '%${instanceName.uppercase()}%'")
        }

        // 查询
        val sql = "SELECT e.*, d.key_ AS processDefinitionKey " +
                "FROM act_ru_execution e LEFT JOIN act_re_procdef d " +
                "ON e.proc_def_id_ = d.id_ " +
                "WHERE $whereStr"
        val query = runtimeService.createNativeProcessInstanceQuery().sql(sql)
        val instances = if (limit < 1) {
            query.list()
        } else {
            query.listPage((pageNum - 1) * limit, limit)
        }

        // 结果处理
        return instances.map { FlowInstance(it) }
    }

    @Transactional
    override fun activate(key: String, bizKey: String) {
        val instanceId = findInstanceId(key, bizKey)
        try {
            runtimeService.activateProcessInstanceById(instanceId)
            log.info("激活流程实例成功！【key：$key，bizKey：$bizKey】")
        } catch (e: ActivitiException) {
            log.warn("忽略流程实例激活操作，因其已处于激活状态！【key：$key，bizKey：$bizKey】")
        }
    }

    @Transactional
    override fun suspend(key: String, bizKey: String) {
        val instanceId = findInstanceId(key, bizKey)
        try {
            runtimeService.suspendProcessInstanceById(instanceId)
            log.info("挂起流程实例成功！【key：$key，bizKey：$bizKey】")
        } catch (e: ActivitiException) {
            log.warn("忽略流程实例挂起操作，因其已处于挂起状态！【key：$key，bizKey：$bizKey】")
        }
    }

    @Transactional
    override fun delete(key: String, bizKey: String, reason: String?) {
        val instanceId = findInstanceId(key, bizKey)
        runtimeService.deleteProcessInstance(instanceId, reason)
        log.info("删除流程实例成功！【bizKey: $bizKey，definitionKey：$key】")
    }

    @Transactional
    override fun updateBizKey(key: String, oldBizKey: String, newBizKey: String) {
        require(newBizKey.isNotBlank()) { "更新流程实例业务主键失败！指定的新业务主键不能为空！【key：$key，oldBizKey: $oldBizKey】" }
        require(!newBizKey.contains("'")) { "更新流程实例业务主键失败！指定的新业务主键不能包含单引号！【key：$key，oldBizKey: $oldBizKey，newBizKey：$newBizKey】" }

        val instanceId = findInstanceId(key, oldBizKey)
        try {
            runtimeService.updateBusinessKey(instanceId, newBizKey)
            log.info("更新流程实例的业务主键成功！【key：$key，oldBizKey: $oldBizKey，newBizKey：$newBizKey】")
        } catch (e: Throwable) {
            val errMsg = "更新流程实例的业务主键失败！【key：$key，oldBizKey: $oldBizKey，newBizKey：$newBizKey】"
            log.error(e, errMsg)
            when (e) {
                is ActivitiIllegalArgumentException, is ActivitiObjectNotFoundException -> {
                    throw ObjectNotFoundException(errMsg, e)
                }
            }
        }
    }

    private fun findInstanceId(key: String, bizKey: String): String {
        val instance = get(key, bizKey)
        if (instance == null) {
            val errMsg = "找不到流程实例！【key：$key，bizKey：$bizKey】"
            log.error(errMsg)
            throw ObjectNotFoundException(errMsg)
        }
        return instance._id
    }

}