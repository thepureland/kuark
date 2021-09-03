package io.kuark.ability.workflow.task

import io.kuark.ability.workflow.definition.FlowDefinitionBizTest
import io.kuark.ability.workflow.instance.IFlowInstanceBiz
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

internal open class FlowTaskBizTest : SpringTest() {

    @Autowired
    private lateinit var flowTaskBiz: IFlowTaskBiz


    @Test
    @Transactional
    open fun isExists() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 存在
        assert(flowTaskBiz.isExists(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY))

        // 不存在
        assertFalse(flowTaskBiz.isExists(NO_EXISTS, APPLICANT_TASK_DEFINITION_KEY))
    }

    @Test
    @Transactional
    open fun get() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 存在
        assertNotNull(flowTaskBiz.get(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY))

        // 不存在
        assertNull(flowTaskBiz.get(instance.bizKey, NO_EXISTS))
    }

    @Test
    @Transactional
    open fun search() {
        FlowDefinitionBizTest.deployThenStart()

        // 有结果
        var criteria = FlowTaskSearchItems.Builder().assignee(APPLICANT_ID).name("申").build()
        assert(flowTaskBiz.search(criteria).isNotEmpty())

        // 无结果
        criteria = FlowTaskSearchItems.Builder().assignee(NO_EXISTS).build()
        assert(flowTaskBiz.search(criteria).isEmpty())
    }

    @Test
    @Transactional
    open fun claim() {
        val instance = FlowDefinitionBizTest.deployThenStart(mapOf("applicantId" to null))

        // 非法参数
        assertThrows<IllegalArgumentException> {
            flowTaskBiz.claim(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, "")
        }
        assertThrows<ObjectNotFoundException> {
            flowTaskBiz.claim(instance.bizKey, NO_EXISTS, NO_EXISTS)
        }

        // 签收无用户的任务
        assert(flowTaskBiz.claim(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID))
        var task = flowTaskBiz.get(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY)!!
        assertEquals(APPLICANT_ID, task.assignee)

        // 相同用户重复签收任务，忽略之
        assert(flowTaskBiz.claim(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID))

        // 签收其他用户的任务，签收失败
        assertFalse(flowTaskBiz.claim(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, "another user id"))
        task = flowTaskBiz.get(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY)!!
        assertEquals(APPLICANT_ID, task.assignee)
    }

    @Test
    @Transactional
    open fun unclaim() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 非法参数
        assertThrows<ObjectNotFoundException> { flowTaskBiz.unclaim(instance.bizKey, NO_EXISTS) }

        // 取消签收已经签收过的任务
        assert(flowTaskBiz.unclaim(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY))
        val task = flowTaskBiz.get(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY)!!
        assertNull(task.assignee)

        // 取消签收未签收过的任务
        assertFalse(flowTaskBiz.unclaim(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY))
    }

    @Test
    @Transactional
    open fun delegate() {
        val instance = FlowDefinitionBizTest.deployThenStart(mapOf("applicantId" to null))

        // 非法参数
        assertThrows<IllegalArgumentException> {
            flowTaskBiz.delegate(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, "")
        }
        assertThrows<ObjectNotFoundException> { flowTaskBiz.delegate(instance.bizKey, NO_EXISTS, APPLICANT_ID) }

        // 无用户签收，则直接做签收操作
        assert(flowTaskBiz.delegate(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID))
        var task = flowTaskBiz.get(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY)!!
        assertEquals(APPLICANT_ID, task.assignee)
        assertNull(task.owner)

        // 将任务委托给其他人
        val newAssignee = "another user id"
        assert(flowTaskBiz.delegate(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, newAssignee))
        task = flowTaskBiz.get(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY)!!
        assertEquals(newAssignee, task.assignee)
        assertEquals(APPLICANT_ID, task.owner)
    }

    @Test
    @Transactional
    open fun complete() {
        val instance = FlowDefinitionBizTest.deployThenStart(
            mapOf(
                APPLICANT_ID to "applicantId",
                DEPT_MANAGER_ID to "deptManagerId",
                DAYS to 2,
                PERSONNEL_ID to "personnelId"
            )
        )

        // 参数非法
        assertThrows<IllegalArgumentException> {
            flowTaskBiz.complete(
                instance.bizKey,
                APPLICANT_TASK_DEFINITION_KEY,
                ""
            )
        }
        assertThrows<ObjectNotFoundException> { flowTaskBiz.complete(instance.bizKey, "non", APPLICANT_ID) }

        // 非任务执行者本人尝试完成任务，操作失败
        assertFalse(flowTaskBiz.complete(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, "another user id"))
        val criteria = FlowTaskSearchItems.Builder().assignee(APPLICANT_ID).build()
        assert(flowTaskBiz.search(criteria).isNotEmpty())

        // 任务执行者本人完成任务
        assert(flowTaskBiz.complete(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID))
        assert(flowTaskBiz.search(criteria).isEmpty())

        // 非任务执行者本人强制完成任务
        assert(flowTaskBiz.complete(instance.bizKey, DEPT_MANAGER_APPROVAL_TASK_KEY, APPLICANT_ID, null, null, true))
        assert(flowTaskBiz.search(criteria).isEmpty())
    }

    @Test
    @Transactional
    open fun revoke() {
        val instance = FlowDefinitionBizTest.deployThenStart(
            mapOf(
                APPLICANT_ID to "applicantId",
                DEPT_MANAGER_ID to "deptManagerId",
                DAYS to 3,
                PERSONNEL_ID to "personnelId",
                GENERAL_MANAGER_IDS to "generalManagerId,assistantGeneralManagerId"
            )
        )

        // 任务执行者本人完成任务
        assert(flowTaskBiz.complete(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID))
        val criteria = FlowTaskSearchItems.Builder().assignee(APPLICANT_ID).build()
        assert(flowTaskBiz.search(criteria).isEmpty())

        // 撤回
        flowTaskBiz.revoke(instance.bizKey, APPLICANT_ID, "填错")
        val tasks = flowTaskBiz.search(criteria)
        assert(tasks.isNotEmpty())
        assertEquals(APPLICANT_TASK_DEFINITION_KEY, tasks.first().definitionKey)

        // 撤回后再次完成任务
        assert(flowTaskBiz.complete(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID))
        assert(flowTaskBiz.search(criteria).isEmpty())

        val complete = flowTaskBiz.complete(instance.bizKey, DEPT_MANAGER_APPROVAL_TASK_KEY, DEPT_MANAGER_ID)

        val r = flowTaskBiz.search(FlowTaskSearchItems.Builder().bizKey(instance.bizKey).build())
        println(r)

        flowTaskBiz.claim(instance.bizKey, GENERAL_MANAGER_APPROVAL_TASK_KEY, "generalManagerId")

        flowTaskBiz.complete(instance.bizKey, GENERAL_MANAGER_APPROVAL_TASK_KEY, "generalManagerId")
        val r2 = flowTaskBiz.search(FlowTaskSearchItems.Builder().bizKey(instance.bizKey).build())
        println(r2)
    }

    @Test
    @Transactional
    open fun reject() {
        val instance = FlowDefinitionBizTest.deployThenStart(
            mapOf(
                APPLICANT_ID to "applicantId",
                DEPT_MANAGER_ID to "deptManagerId",
                DAYS to 3,
                PERSONNEL_ID to "personnelId"
            )
        )

        // 任务执行者本人完成任务
        assert(flowTaskBiz.complete(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID))
        val criteria = FlowTaskSearchItems.Builder().assignee(APPLICANT_ID).build()
        assert(flowTaskBiz.search(criteria).isEmpty())

        // 驳回
        flowTaskBiz.reject(instance.bizKey, DEPT_MANAGER_APPROVAL_TASK_KEY, DEPT_MANAGER_ID, "3天不予准备")
        val tasks = flowTaskBiz.search(criteria)
        assert(tasks.isNotEmpty())
        assertEquals(APPLICANT_TASK_DEFINITION_KEY, tasks.first().definitionKey)

        // 驳回后再次完成任务
        val variables = mapOf(DAYS to 2)
        assert(flowTaskBiz.complete(instance.bizKey, APPLICANT_TASK_DEFINITION_KEY, APPLICANT_ID, null, variables))
        assert(flowTaskBiz.search(criteria).isEmpty())
    }

    @Test
    @Transactional
    open fun fullTest() {

    }

    internal companion object {
        const val NO_EXISTS = "no exists"
        const val APPLICANT_ID = "applicantId" // 申请人id
        const val APPLICANT_TASK_DEFINITION_KEY = "applicationTaskKey"
        const val DEPT_MANAGER_ID = "deptManagerId" // 部门经理id
        const val DEPT_MANAGER_APPROVAL_TASK_KEY = "deptManagerApprovalTaskKey"
        const val GENERAL_MANAGER_APPROVAL_TASK_KEY = "generalManagerApprovalTaskKey"
        const val GENERAL_MANAGER_IDS = "generalManagerIds"
        const val PERSONNEL_APPROVAL_TASK_KEY = "personnelApprovalTaskKey"
        const val PERSONNEL_ID = "personnelId"
        const val DAYS = "days"
    }

}