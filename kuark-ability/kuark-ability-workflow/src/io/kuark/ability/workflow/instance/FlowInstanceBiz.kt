package io.kuark.ability.workflow.instance

import io.kuark.ability.workflow.event.IFlowEventListener
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.log.LogFactory
import org.activiti.engine.ActivitiException
import org.activiti.engine.ActivitiIllegalArgumentException
import org.activiti.engine.ActivitiObjectNotFoundException
import org.activiti.engine.RuntimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

/**
 * 流程实例相关业务
 *
 * @author K
 * @since 1.0.0
 */
open class FlowInstanceBiz : IFlowInstanceBiz {

    @Autowired
    private lateinit var runtimeService: RuntimeService

    private val log = LogFactory.getLog(this::class)

    override fun getFlowInstance(bizKey: String, definitionKey: String?): FlowInstance? {
        var query = runtimeService.createProcessInstanceQuery()
            .processInstanceBusinessKey(bizKey)
        if (definitionKey != null) {
            query = query.processDefinitionKey(definitionKey)
        }
        val instance = query.singleResult()
        return if (instance == null) null else FlowInstance(instance)
    }

    override fun getFlowInstances(definitionKey: String): List<FlowInstance> {
        val instances = runtimeService.createProcessInstanceQuery()
            .processDefinitionKey(definitionKey)
            .list()
        return instances.map { FlowInstance(it) }
    }

    @Transactional
    override fun startInstance(
        definitionKey: String,
        bizKey: String,
        instanceName: String,
        variables: Map<String, *>?,
        eventListener: IFlowEventListener?
    ): FlowInstance? {
        val instance = try {
            if (eventListener != null) {
                runtimeService.addEventListener(eventListener)
            }
            runtimeService.startProcessInstanceByKey(definitionKey, bizKey, variables).apply {
                log.info("启动流程实例成功！definitionKey：$definitionKey，bizKey：$bizKey，instanceName：$instanceName")
            }
        } catch (e: ActivitiObjectNotFoundException) {
            log.error("启动流程实例失败，因流程定义不存在！definitionKey：$definitionKey，bizKey：$bizKey")
            null
        } catch (e: Throwable) {
            log.error(e, "启动流程实例失败！definitionKey: $definitionKey, bizKey: $bizKey")
            null
        }
        return if (instance == null) {
            null
        } else {
            runtimeService.setProcessInstanceName(instance.processInstanceId, instanceName)
            FlowInstance(instance).apply {
                this.name = instanceName
            }
        }
    }

    @Transactional
    override fun activateInstance(bizKey: String, definitionKey: String?) {
        val instanceId = findInstanceId(bizKey, definitionKey)
        try {
            runtimeService.activateProcessInstanceById(instanceId)
            log.info("激活流程实例成功！bizKey：$bizKey，definitionKey：$definitionKey")
        } catch (e: ActivitiObjectNotFoundException) {
            val errMsg = "激活流程实例时找不到流程实例！bizKey: $bizKey，definitionKey：$definitionKey"
            log.error(e, errMsg)
            throw IllegalArgumentException(errMsg, e)
        } catch (e: ActivitiException) {
            log.warn("忽略流程实例激活操作,因其已处于激活状态！bizKey：$bizKey，definitionKey：$definitionKey")
        }
    }

    @Transactional
    override fun suspendInstance(bizKey: String, definitionKey: String?) {
        val instanceId = findInstanceId(bizKey, definitionKey)
        try {
            runtimeService.suspendProcessInstanceById(instanceId)
            log.info("挂起流程实例成功！bizKey：$bizKey，definitionKey：$definitionKey")
        } catch (e: ActivitiObjectNotFoundException) {
            val errMsg = "挂起流程实例时找不到流程实例！bizKey: $bizKey，definitionKey：$definitionKey"
            log.error(e, errMsg)
            throw IllegalArgumentException(errMsg, e)
        } catch (e: ActivitiException) {
            log.warn("忽略流程实例挂起操作,因其已处于挂起状态！bizKey：$bizKey，definitionKey：$definitionKey")
        }
    }

    @Transactional
    override fun deleteInstance(bizKey: String, reason: String, definitionKey: String?) {
        val instanceId = findInstanceId(bizKey, definitionKey)
        try {
            runtimeService.deleteProcessInstance(instanceId, reason)
            log.info("删除流程实例成功！bizKey: $bizKey，definitionKey：$definitionKey")
        } catch (e: ActivitiObjectNotFoundException) {
            val errMsg = "删除流程实例时找不到流程实例！bizKey: $bizKey，definitionKey：$definitionKey"
            log.error(e, errMsg)
            throw IllegalArgumentException(errMsg, e)
        }
    }

    @Transactional
    override fun updateBizKey(oldBizKey: String, newBizKey: String, definitionKey: String?) {
        val instanceId = findInstanceId(oldBizKey, definitionKey)
        try {
            runtimeService.updateBusinessKey(instanceId, newBizKey)
            log.info("更新流程实例的bizKey成功！oldBizKey: $oldBizKey，definitionKey：$definitionKey，newBizKey：$newBizKey")
        } catch (e: Throwable) {
            val errMsg = "更新流程实例的bizKey失败！oldBizKey: $oldBizKey，definitionKey：$definitionKey，newBizKey：$newBizKey"
            log.error(e, errMsg)
            when (e) {
                is ActivitiIllegalArgumentException, is ActivitiObjectNotFoundException -> {
                    throw IllegalArgumentException(errMsg, e)
                }
            }
        }
    }

    private fun findInstanceId(bizKey: String, definitionKey: String?): String {
        val instance = getFlowInstance(bizKey, definitionKey)
        if (instance == null) {
            val errMsg = "找不到流程实例！bizKey：$bizKey，definitionKey：$definitionKey"
            log.error(errMsg)
            throw ObjectNotFoundException(errMsg)
        }
        return instance._id
    }

}