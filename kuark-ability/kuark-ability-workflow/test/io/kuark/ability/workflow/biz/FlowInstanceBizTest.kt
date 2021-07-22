package io.kuark.ability.workflow.biz

import io.kuark.ability.workflow.ibiz.IFlowDefinitionBiz
import io.kuark.ability.workflow.ibiz.IFlowInstanceBiz
import io.kuark.ability.workflow.vo.FlowDefinition
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal open class FlowInstanceBizTest: SpringTest() {

    @Autowired
    private lateinit var flowDefinitionBiz: IFlowDefinitionBiz

    @Autowired
    private lateinit var flowInstanceBiz: IFlowInstanceBiz

    private val BIZ_KEY = "bizKey"

    @Test
    fun getFlowInstance() {
        val definition = deploy()
        val instance = flowInstanceBiz.startInstance(definition.key, BIZ_KEY)
        val flowInstance = flowInstanceBiz.getFlowInstance(BIZ_KEY)
        assertNotNull(instance)
        assertNotNull(flowInstance)
        assertEquals(instance!!.id, flowInstance!!.id)
    }

    @Test
    fun getFlowInstances() {
    }

    @Test
    fun startInstance() {
    }

    @Test
    fun activateInstance() {
    }

    @Test
    fun suspendInstance() {
    }

    @Test
    fun updateBizKey() {
    }

    private fun deploy(): FlowDefinition {
        return flowDefinitionBiz.deployWithBpmn("请假申请(junit)", "test", "test")
    }

}