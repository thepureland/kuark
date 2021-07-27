package io.kuark.ability.workflow.biz

import io.kuark.ability.workflow.ibiz.IFlowTaskBiz
import io.kuark.ability.workflow.vo.FlowTask
import io.kuark.base.log.LogFactory
import org.activiti.engine.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

/**
 * 流程任务相关业务
 *
 * @author K
 * @since 1.0.0
 */
open class FlowTaskBiz : IFlowTaskBiz {

    @Autowired
    private lateinit var taskService: TaskService

    private val log = LogFactory.getLog(this::class)

    override fun getTask(bizKey: String, taskDefinitionKey: String): FlowTask? {
        val task = taskService.createTaskQuery()
            .processInstanceBusinessKey(bizKey)
            .taskDefinitionKey(taskDefinitionKey)
            .singleResult()
        return if (task == null) null else FlowTask(task)
    }

    override fun getTasksByUser(vararg userIds: String): List<FlowTask> {
        require(userIds.isNotEmpty()) { log.error("FlowTaskBiz::getTasksByUser的userIds参数不能为空！") }
        val tasks = taskService.createTaskQuery()
            .taskAssigneeIds(userIds.toList())
            .list()
        return tasks.map { FlowTask(it) }
    }

    @Transactional
    override fun claimTask(bizKey: String, taskDefinitionKey: String, userId: String): Boolean {
        val userId = userId.trim()
        require(userId.isNotEmpty()) { log.error("FlowTaskBiz::claimTask的userId参数不能为空！") }
        val task = findTask(bizKey, taskDefinitionKey)
        if (task.assignee == null || task.assignee.isEmpty()) {
            taskService.claim(task._id, userId)
            log.info("流程任务用户绑定成功！bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey，userId: $userId")
        } else {
            if (task.assignee == userId) {
                log.warn("忽略流程任务用户绑定操作，因该用户已绑定！bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey，userId: $userId")
            } else {
                log.error("流程任务用户绑定失败，因已有其他用户绑定，要更改绑定的用户，请先解绑！bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey，userId: $userId")
                return false
            }
        }
        return true
    }

    @Transactional
    override fun unclaimTask(bizKey: String, taskDefinitionKey: String): Boolean {
        val task = findTask(bizKey, taskDefinitionKey)
        return if (task.assignee == null || task.assignee.isEmpty()) {
            log.warn("忽略流程任务用户解绑操作，因该任务无绑定的用户！bizKey：bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey")
            false
        } else {
            taskService.unclaim(task._id)
            log.info("流程任务用户解绑成功！bizKey：bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey")
            true
        }
    }

    @Transactional
    override fun completeTask(bizKey: String, taskDefinitionKey: String, userId: String, force: Boolean): Boolean {
        val userId = userId.trim()
        require(userId.isNotEmpty()) { log.error("FlowTaskBiz::completeTask的userId参数不能为空！") }
        val task = findTask(bizKey, taskDefinitionKey)
        if (force) {
            taskService.setAssignee(task._id, userId)
            taskService.complete(task._id)
            log.info("强制执行流程任务成功！bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey, userId: $userId")
        } else {
            if (userId == task.assignee) {
                taskService.complete(task._id)
                log.info("执行流程任务成功！bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey, userId: $userId")
            } else {
                log.error("执行流程任务失败，因绑定的用户不是$userId！bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey, userId: $userId")
                return false
            }
        }
        return true
    }

    private fun findTask(bizKey: String, taskDefinitionKey: String): FlowTask {
        val task = getTask(bizKey, taskDefinitionKey)
        if (task == null) {
            val errMsg = "找不到流程任务！bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey"
            log.error(errMsg)
            throw IllegalArgumentException(errMsg)
        }
        return task
    }

}