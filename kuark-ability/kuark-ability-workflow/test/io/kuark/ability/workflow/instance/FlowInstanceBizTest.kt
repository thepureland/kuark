package io.kuark.ability.workflow.instance

import io.kuark.ability.workflow.definition.FlowDefinitionBizTest
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

internal open class FlowInstanceBizTest : SpringTest() {


    @Autowired
    private lateinit var flowInstanceBiz: IFlowInstanceBiz

    private val BIZ_KEY = FlowDefinitionBizTest.BIZ_KEY
    private val INSTANCE_NAME = FlowDefinitionBizTest.INSTANCE_NAME
    private val NO_EXISTS = "no exists"


    @Test
    @Transactional
    open fun isExists() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        assert(flowInstanceBiz.isExists(instance.definitionKey, BIZ_KEY))
        assert(flowInstanceBiz.isExists(instance.definitionKey, BIZ_KEY, 1))
        assert(flowInstanceBiz.isExists(instance.definitionKey, BIZ_KEY, 1))
        assertFalse(flowInstanceBiz.isExists(NO_EXISTS, BIZ_KEY, 1))
        assertFalse { flowInstanceBiz.isExists(" ", BIZ_KEY) }
        assertFalse { flowInstanceBiz.isExists(instance.definitionKey, "") }
    }

    @Test
    @Transactional
    open fun get() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 正常结果，未指定版本号
        var flowInstance = flowInstanceBiz.get(instance.definitionKey, BIZ_KEY)
        assertNotNull(instance)
        assertNotNull(flowInstance)
        assertEquals(instance._id, flowInstance!!._id)

        // 正常结果，指定版本号
        flowInstance = flowInstanceBiz.get(instance.definitionKey, BIZ_KEY, 1)
        assertNotNull(instance)
        assertNotNull(flowInstance)
        assertEquals(instance._id, flowInstance!!._id)

        // 传不存在的key
        assertNull(flowInstanceBiz.get(NO_EXISTS, BIZ_KEY))
    }

    @Test
    @Transactional
    open fun search() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 正常结果
        val builder = FlowInstanceQueryItems.Builder().key(instance.definitionKey)
        assert(flowInstanceBiz.search(builder.build()).isNotEmpty())
        assert(flowInstanceBiz.search(builder.bizKey(BIZ_KEY).build()).isNotEmpty())
        var criteria = builder.bizKey(BIZ_KEY).instanceName(INSTANCE_NAME).build()
        assert(flowInstanceBiz.search(criteria).isNotEmpty())
        assert(flowInstanceBiz.search(criteria, 1, 10).isNotEmpty())
        criteria = builder.key("eaveAppl").bizKey("bizKe").instanceName("INSTANCE").build()
        assert(flowInstanceBiz.search(criteria).isNotEmpty())

        // 传不存在的流程定义key
        criteria = builder.key(NO_EXISTS).build()
        assert(flowInstanceBiz.search(criteria).isEmpty())
    }

    @Test
    @Transactional
    open fun activate() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 找不到流程实例
        assertThrows<ObjectNotFoundException> { flowInstanceBiz.activate(NO_EXISTS, BIZ_KEY) }

        // 成功激活
        flowInstanceBiz.activate(instance.definitionKey, BIZ_KEY)
        var flowInstance = flowInstanceBiz.get(instance.definitionKey, BIZ_KEY)!!
        assertEquals(FlowInstanceStatus.RUNNING, flowInstance.status)

        // 重复激活
        flowInstanceBiz.activate(instance.definitionKey, BIZ_KEY)
        flowInstance = flowInstanceBiz.get(instance.definitionKey, BIZ_KEY)!!
        assertEquals(FlowInstanceStatus.RUNNING, flowInstance.status)
    }

    @Test
    @Transactional
    open fun suspend() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 找不到流程实例
        assertThrows<ObjectNotFoundException> { flowInstanceBiz.suspend(instance.definitionKey, NO_EXISTS) }

        // 成功挂起
        flowInstanceBiz.suspend(instance.definitionKey, BIZ_KEY)
        var flowInstance = flowInstanceBiz.get(instance.definitionKey, BIZ_KEY)!!
        assertEquals(FlowInstanceStatus.SUSPENDED, flowInstance.status)

        // 重复挂起
        flowInstanceBiz.suspend(instance.definitionKey, BIZ_KEY)
        flowInstance = flowInstanceBiz.get(instance.definitionKey, BIZ_KEY)!!
        assertEquals(FlowInstanceStatus.SUSPENDED, flowInstance.status)
    }

    @Test
    @Transactional
    open fun delete() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 找不到流程实例
        assertThrows<ObjectNotFoundException> {
            flowInstanceBiz.delete(instance.definitionKey, NO_EXISTS, "test")
        }

        // 成功删除
        flowInstanceBiz.delete(instance.definitionKey, BIZ_KEY, "test")
        assertNull(flowInstanceBiz.get(instance.definitionKey, BIZ_KEY))
        var criteria = FlowInstanceQueryItems.Builder().key(instance.definitionKey).build()
        assert(flowInstanceBiz.search(criteria).isEmpty())
    }

    @Test
    @Transactional
    open fun updateBizKey() {
        val instance = FlowDefinitionBizTest.deployThenStart()
        val newBizKey = "new biz key"

        // 参数非法
        assertThrows<IllegalArgumentException> {
            flowInstanceBiz.updateBizKey(instance.definitionKey, BIZ_KEY, "")
        }
        assertThrows<IllegalArgumentException> {
            flowInstanceBiz.updateBizKey(instance.definitionKey, BIZ_KEY, "biz'key")
        }

        // 流程实例找不到
        assertThrows<ObjectNotFoundException> {
            flowInstanceBiz.updateBizKey(instance.definitionKey, NO_EXISTS, newBizKey)
        }

        // 更新成功
        flowInstanceBiz.updateBizKey(instance.definitionKey, BIZ_KEY, newBizKey)
        assertNotNull(flowInstanceBiz.get(instance.definitionKey, newBizKey))
    }

}