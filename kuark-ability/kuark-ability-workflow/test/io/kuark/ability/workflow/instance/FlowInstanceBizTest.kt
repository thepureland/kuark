package io.kuark.ability.workflow.instance

import io.kuark.ability.workflow.definition.IFlowDefinitionBiz
import io.kuark.ability.workflow.definition.FlowDefinition
import io.kuark.ability.workflow.event.FlowEvent
import io.kuark.ability.workflow.event.FlowEventType
import io.kuark.ability.workflow.event.IFlowEventListener
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

internal open class FlowInstanceBizTest : SpringTest() {

    @Autowired
    private lateinit var flowDefinitionBiz: IFlowDefinitionBiz

    @Autowired
    private lateinit var flowInstanceBiz: IFlowInstanceBiz

    private val BIZ_KEY = "bizKey"
    private val NO_EXISTS = "no exists"
    private val APPLICANT_ID = "applicantId"

    @Test
    @Transactional
    open fun getFlowInstance() {
        val instance = deployThenStart()

        // 正常结果，只传业务主键
        var flowInstance = flowInstanceBiz.getFlowInstance(BIZ_KEY)
        assertNotNull(instance)
        assertNotNull(flowInstance)
        assertEquals(instance!!._id, flowInstance!!._id)

        // 正常结果，传业务主键和流程定义key
        flowInstance = flowInstanceBiz.getFlowInstance(BIZ_KEY, instance.definitionKey)
        assertNotNull(instance)
        assertNotNull(flowInstance)
        assertEquals(instance._id, flowInstance!!._id)

        // 传不存在的业务主键
        assertNull(flowInstanceBiz.getFlowInstance(NO_EXISTS))
    }

    @Test
    @Transactional
    open fun getFlowInstances() {
        val instance = deployThenStart()

        // 正常结果
        assertEquals(1, flowInstanceBiz.getFlowInstances(instance!!.definitionKey).size)

        // 传不存在的流程定义key
        assert(flowInstanceBiz.getFlowInstances(NO_EXISTS).isEmpty())
    }

    @Test
    @Transactional
    open fun startInstance() {
        // 传不存在的流程定义key
        assertNull(flowInstanceBiz.startInstance(NO_EXISTS, BIZ_KEY, "instanceName"))

        // 传监听器
        var isEventFire = false
        val listener = object : IFlowEventListener {
            override fun onEvent(event: FlowEvent) {
                if (event.type == FlowEventType.ACTIVITY_STARTED) {
                    isEventFire = true
                }
            }
        }
        val definition = deploy()
        val instance = flowInstanceBiz.startInstance(
            definition.key, BIZ_KEY, "instanceName", mapOf("applicantId" to APPLICANT_ID), listener
        )
        assertNotNull(instance)
        assertNotNull(flowInstanceBiz.getFlowInstance(BIZ_KEY, instance!!.definitionKey))
        assert(isEventFire)
    }

    @Test
    @Transactional
    open fun activateInstance() {
        deployThenStart()

        // 正常激活
        flowInstanceBiz.activateInstance(BIZ_KEY)
        var flowInstance = flowInstanceBiz.getFlowInstance(BIZ_KEY)!!
        assertEquals(FlowInstanceStatus.RUNNING, flowInstance.status)

        // 传不存在的业务主键
        assertThrows<ObjectNotFoundException> { flowInstanceBiz.activateInstance(NO_EXISTS) }

        // 重复激活
        flowInstanceBiz.activateInstance(BIZ_KEY)
        flowInstance = flowInstanceBiz.getFlowInstance(BIZ_KEY)!!
        assertEquals(FlowInstanceStatus.RUNNING, flowInstance.status)
    }

    @Test
    @Transactional
    open fun suspendInstance() {
        deployThenStart()

        // 正常挂起
        flowInstanceBiz.suspendInstance(BIZ_KEY)
        val flowInstance = flowInstanceBiz.getFlowInstance(BIZ_KEY)!!
        assertEquals(FlowInstanceStatus.SUSPENDED, flowInstance.status)

        // 非法参数
        assertThrows<ObjectNotFoundException> { flowInstanceBiz.suspendInstance(NO_EXISTS) }
    }

    @Test
    @Transactional
    open fun deleteInstance() {
        val instance = deployThenStart()!!
        flowInstanceBiz.deleteInstance(BIZ_KEY, "test")
        assertNull(flowInstanceBiz.getFlowInstance(BIZ_KEY))
        assert(flowInstanceBiz.getFlowInstances(instance.definitionKey).isEmpty())

        assertThrows<ObjectNotFoundException> { flowInstanceBiz.deleteInstance(NO_EXISTS, "test") }
    }

    @Test
    @Transactional
    open fun updateBizKey() {
        deployThenStart()!!
        val newBizKey = "new biz key"
        flowInstanceBiz.updateBizKey(BIZ_KEY, newBizKey)
        assertNotNull(flowInstanceBiz.getFlowInstance(newBizKey))
    }

    private fun deploy(): FlowDefinition {
        return flowDefinitionBiz.deployWithBpmn("请假申请(junit)", "test", "test")
    }

    private fun deployThenStart(): FlowInstance? {
        val definition = deploy()
        return flowInstanceBiz.startInstance(
            definition.key, BIZ_KEY, "instanceName", mapOf("applicantId" to APPLICANT_ID)
        )
    }

}