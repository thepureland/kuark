package io.kuark.ability.workflow.biz

import io.kuark.ability.workflow.ibiz.IFlowDefinitionBiz
import io.kuark.ability.workflow.ibiz.IFlowInstanceBiz
import io.kuark.ability.workflow.ibiz.IFlowTaskBiz
import io.kuark.ability.workflow.vo.FlowDefinition
import io.kuark.ability.workflow.vo.FlowInstance
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

internal open class FlowTaskBizTest : SpringTest() {

    @Autowired
    private lateinit var flowDefinitionBiz: IFlowDefinitionBiz

    @Autowired
    private lateinit var flowInstanceBiz: IFlowInstanceBiz

    @Autowired
    private lateinit var flowTaskBiz: IFlowTaskBiz

    private val BIZ_KEY = "bizKey"
    private val NO_EXISTS = "no exists"
    private val APPLICANT_ID = "applicantId" // 申请人id
    private val APPLICANT_TASK_DEFINITION_KEY = "applicationTaskKey"
    private val APPROVER_ID = "approverId" // 审批人id
    private val APPROVAL_TASK_DEFINITION_KEY = "approvalTaskKey"

    @Test
    @Transactional
    open fun getTask() {
        deployThenStart()

        // 正常结果
        assertNotNull(flowTaskBiz.getTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY))

        // 传不存在的taskDefinitionKey
        assertNull(flowTaskBiz.getTask(BIZ_KEY, NO_EXISTS))
    }

    @Test
    @Transactional
    open fun getTasksByUser() {
        deployThenStart()

        // 正常结果
        assert(flowTaskBiz.getTasksByUser(APPLICANT_ID).isNotEmpty())

        // 传空数组
        assertThrows<IllegalArgumentException> { flowTaskBiz.getTasksByUser() }

        // 传不存在的用户id
        assert(flowTaskBiz.getTasksByUser(NO_EXISTS).isEmpty())
    }

    @Test
    @Transactional
    open fun claimTask() {
        deployThenStart(mapOf("applicantId" to null))

        // 非法参数
        assertThrows<IllegalArgumentException> {
            flowTaskBiz.claimTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY, "")
        }
        assertThrows<IllegalArgumentException> {
            flowTaskBiz.claimTask(BIZ_KEY, NO_EXISTS, NO_EXISTS)
        }

        // 绑定无用户的任务
        assert(flowTaskBiz.claimTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID))
        var task = flowTaskBiz.getTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY)!!
        assertEquals(APPLICANT_ID, task.assignee)

        // 重复绑定相同用户的任务，忽略绑定
        assert(flowTaskBiz.claimTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID))

        // 绑定已有用户的任务，绑定失败
        assertFalse(flowTaskBiz.claimTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY, "another user id"))
        task = flowTaskBiz.getTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY)!!
        assertEquals(APPLICANT_ID, task.assignee)
    }

    @Test
    @Transactional
    open fun unclaimTask() {
        deployThenStart()

        // 非法参数
        assertThrows<IllegalArgumentException> { flowTaskBiz.unclaimTask(BIZ_KEY, NO_EXISTS) }

        // 解绑有用户的任务
        assert(flowTaskBiz.unclaimTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY))
        val task = flowTaskBiz.getTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY)!!
        assertNull(task.assignee)

        // 解绑无用户的任务
        assertFalse(flowTaskBiz.unclaimTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY))
    }

    @Test
    @Transactional
    open fun completeTask() {
        deployThenStart(mapOf("applicantId" to APPLICANT_ID, "approverId" to APPROVER_ID))

        // 参数非法
        assertThrows<IllegalArgumentException> { flowTaskBiz.completeTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY, "") }
        assertThrows<IllegalArgumentException> { flowTaskBiz.completeTask(BIZ_KEY, "non", APPLICANT_ID) }

        // 非任务执行者本人尝试完成任务，操作失败
        assertFalse(flowTaskBiz.completeTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY, "another user id"))
        assert(flowTaskBiz.getTasksByUser(APPLICANT_ID).isNotEmpty())

        // 任务执行者本人完成任务
        assert(flowTaskBiz.completeTask(BIZ_KEY, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID))
        assert(flowTaskBiz.getTasksByUser(APPLICANT_ID).isEmpty())
        assert(flowTaskBiz.getTasksByUser(APPROVER_ID).isNotEmpty())

        // 非任务执行者本人强制完成任务
        assert(flowTaskBiz.completeTask(BIZ_KEY, APPROVAL_TASK_DEFINITION_KEY, APPLICANT_ID, true))
        assert(flowTaskBiz.getTasksByUser(APPROVER_ID).isEmpty())
    }

    private fun deploy(): FlowDefinition {
        return flowDefinitionBiz.deployWithBpmn("请假申请(junit)", "test", "test")
    }

    private fun deployThenStart(variables: Map<String, *>? = mapOf("applicantId" to APPLICANT_ID)): FlowInstance? {
        val definition = deploy()
        return flowInstanceBiz.startInstance(definition.key, BIZ_KEY, "instanceName", variables)
    }

}