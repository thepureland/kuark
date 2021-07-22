package io.kuark.ability.workflow.vo

import org.activiti.engine.task.Task
import java.util.*

/**
 * 流程任务数据类
 *
 * @author K
 * @since 1.0.0
 */
data class FlowTask(
    val id: String,
    val name: String,
    val definitionId: String,
    val instanceId: String,
    val assignee: String,
    val localVariables: Map<String, Any>,
    val flowVariables: Map<String, Any>
) {

    private var owner: String? = null
    private var description: String? = null
    private var createdTime: Date? = null
    private var claimedTime: Date? = null
    private var dueDate: Date? = null
    private var priority = 0
    private var parentTaskId: String? = null
    private var taskDefinitionKey: String? = null

    constructor(task: Task) : this(
        task.id,
        task.name,
        task.processDefinitionId,
        task.processInstanceId,
        task.assignee,
        task.taskLocalVariables,
        task.processVariables
    ) {
        this.owner = task.owner
        this.description = task.description
        this.createdTime = task.createTime
        this.claimedTime= task.claimTime
        this.dueDate = task.dueDate
        this.priority = task.priority
        this.parentTaskId = task.parentTaskId
        this.taskDefinitionKey = task.taskDefinitionKey
    }

}