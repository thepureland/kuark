package io.kuark.ability.workflow.biz

import io.kuark.ability.workflow.ibiz.IFlowInstanceBiz
import io.kuark.ability.workflow.vo.FlowInstance
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

    override fun getFlowInstance(bizKey: String): FlowInstance? {
        val instance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(bizKey).singleResult()
        return if (instance == null) null else FlowInstance(instance)
    }

    override fun getFlowInstances(definitionKey: String): List<FlowInstance> {
        val instances = runtimeService.createProcessInstanceQuery().processDefinitionKey(definitionKey).list()
        return instances.map { FlowInstance(it) }
    }

    @Transactional
    override fun startInstance(definitionKey: String, bizKey: String, variables: Map<String, *>?): FlowInstance? {
        val instance = try {
            runtimeService.startProcessInstanceByKey(definitionKey, bizKey, variables)
        } catch (e: ActivitiObjectNotFoundException) {
            log.error("启动流程实例失败！definitionKey: ${definitionKey}对应的流程定义不存在！")
            null
        } catch (e: Throwable) {
            log.error(e, "启动流程实例失败！definitionKey: $definitionKey")
            null
        }
        return if (instance == null) null else FlowInstance(instance)
    }

    @Transactional
    override fun activateInstance(instanceId: String) {
        try {
            runtimeService.activateProcessInstanceById(instanceId)
        } catch (e: ActivitiObjectNotFoundException) {
            log.error(e)
            throw IllegalArgumentException(e)
        } catch (e: ActivitiException) {
            log.warn("instanceId: ${instanceId}对应的流程实例已经处于激活状态，忽略对其激活操作!")
        }
    }

    @Transactional
    override fun suspendInstance(instanceId: String) {
        try {
            runtimeService.suspendProcessInstanceById(instanceId)
        } catch (e: ActivitiObjectNotFoundException) {
            log.error(e)
            throw IllegalArgumentException(e)
        } catch (e: ActivitiException) {
            log.warn("instanceId: ${instanceId}对应的流程实例已经处于挂起状态，忽略对其挂起操作!")
        }
    }

    @Transactional
    override fun updateBizKey(instanceId: String, bizKey: String) {
        try {
            runtimeService.updateBusinessKey(instanceId, bizKey)
        } catch (e: Throwable) {
            log.error(e)
            when (e) {
                is ActivitiIllegalArgumentException, is ActivitiObjectNotFoundException -> {
                    throw IllegalArgumentException(e)
                }
            }
        }
    }


}