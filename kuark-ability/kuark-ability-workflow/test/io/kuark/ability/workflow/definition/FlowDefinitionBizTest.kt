package io.kuark.ability.workflow.definition

import io.kuark.ability.workflow.event.FlowEvent
import io.kuark.ability.workflow.event.FlowEventType
import io.kuark.ability.workflow.event.IFlowEventListener
import io.kuark.ability.workflow.instance.FlowInstance
import io.kuark.ability.workflow.model.FlowModelBizTest
import io.kuark.base.error.ObjectAlreadyExistsException
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.context.spring.SpringKit
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional


internal open class FlowDefinitionBizTest : SpringTest() {

    @Autowired
    private lateinit var flowDefinitionBiz: IFlowDefinitionBiz

    private val NO_EXISTS = "no exists"

    @Test
    @Transactional
    open fun isExists() {
        deploy()

        assert(flowDefinitionBiz.isExists(FlowModelBizTest.KEY, 1))
        assert(flowDefinitionBiz.isExists(FlowModelBizTest.KEY))
        assertFalse(flowDefinitionBiz.isExists(FlowModelBizTest.KEY, 2))
        assertFalse(flowDefinitionBiz.isExists(NO_EXISTS, 1))
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.isExists(" ") }
    }

    @Test
    @Transactional
    open fun get() {
        deploy()

        assertNotNull(flowDefinitionBiz.get(FlowModelBizTest.KEY, 1))
        assertNull(flowDefinitionBiz.get(FlowModelBizTest.KEY, 2))
        assertNull(flowDefinitionBiz.get(NO_EXISTS, 1))
        assertEquals(1, flowDefinitionBiz.get(FlowModelBizTest.KEY)!!.version)
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.get(" ") }
    }

    @Test
    @Transactional
    open fun search() {
        deploy()

        // 指定条件模糊搜索
        var criteria = FlowDefinitionCriteria.Builder().key("leaveAp").name("请假").latestOnly(true).build()
        val definitions = flowDefinitionBiz.search(criteria, 1, 1)
        assertEquals(1, definitions.size)

        // 没有指定条件
        criteria = FlowDefinitionCriteria.Builder().build()
        assertEquals(1, flowDefinitionBiz.search(criteria).size)
    }

    @Test
    @Transactional
    open fun activate() {
        val definition = deploy()
        flowDefinitionBiz.activate(definition.key)
    }

    @Test
    @Transactional
    open fun suspend() {
        val definition = deploy()
        flowDefinitionBiz.suspend(definition.key)
    }

    @Test
    @Transactional
    open fun delete() {
        val definition = deploy()
        flowDefinitionBiz.delete(definition.key, null, true)
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.delete(NO_EXISTS, null, true) }
    }

    @Test
    @Transactional
    open fun start() {
        val definition = deploy()

        // 非法参数
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start("", BIZ_KEY, INSTANCE_NAME) }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start(KEY, " ", INSTANCE_NAME) }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start(KEY, "biz'key", INSTANCE_NAME) }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start(KEY, BIZ_KEY, "") }

        // 传不存在的流程定义key
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.start(NO_EXISTS, BIZ_KEY, INSTANCE_NAME) }

        // 成功启动
        var isEventFire = false
        val listener = object : IFlowEventListener {
            override fun onEvent(event: FlowEvent) {
                if (event.type == FlowEventType.ACTIVITY_STARTED) {
                    isEventFire = true
                }
            }
        }
        var variables = mapOf("applicantId" to APPLICANT_ID)
        val instance = flowDefinitionBiz.start(definition.key, BIZ_KEY, INSTANCE_NAME, variables, listener)
        assertNotNull(instance)
        assertNotNull(instance!!._id)
        assert(isEventFire)
        assertEquals(INSTANCE_NAME, instance.name)

        // 重复启动
        assertThrows<ObjectAlreadyExistsException> {
            flowDefinitionBiz.start(definition.key, BIZ_KEY, INSTANCE_NAME, variables, listener)
        }
    }


    companion object {

        internal val KEY = FlowModelBizTest.KEY
        internal val BIZ_KEY = "bizKey"
        internal val INSTANCE_NAME = "instanceName"
        internal val APPLICANT_ID = "applicantId"

        internal fun deploy(): FlowDefinition {
            FlowModelBizTest.createModel()
            return FlowModelBizTest.deployModel()
        }

        internal fun deployThenStart(variables: Map<String, *>? = mapOf("applicantId" to APPLICANT_ID)): FlowInstance {
            val flowDefinitionBiz = SpringKit.getBean(IFlowDefinitionBiz::class)
            deploy()
            return flowDefinitionBiz.start(FlowModelBizTest.KEY, BIZ_KEY, INSTANCE_NAME, variables)!!
        }

    }

}