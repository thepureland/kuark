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
    internal val _id: String,
    val name: String,
    val definitionKey: String,
    val assignee: String?
) {

    private var _parentTaskId: String? = null
    private var _flowDefinitionId: String? = null
    private var _instanceId: String? = null
    private var localVariables: Map<String, Any>? = null
    private var flowVariables: Map<String, Any>? = null
    private var owner: String? = null
    private var description: String? = null
    private var createdTime: Date? = null
    private var claimedTime: Date? = null
    private var dueDate: Date? = null
    private var priority = 0


    constructor(task: Task) : this(
        task.id,
        task.name,
        task.taskDefinitionKey,
        task.assignee
    ) {
        this._flowDefinitionId = task.processDefinitionId
        this._instanceId = task.processInstanceId
        this.localVariables = task.taskLocalVariables
        this.flowVariables = task.processVariables
        this.owner = task.owner
        this.description = task.description
        this.createdTime = task.createTime
        this.claimedTime= task.claimTime
        this.dueDate = task.dueDate
        this.priority = task.priority
        this._parentTaskId = task.parentTaskId
    }

}