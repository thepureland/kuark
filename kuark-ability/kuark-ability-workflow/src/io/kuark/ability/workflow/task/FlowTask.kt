package io.kuark.ability.workflow.task

import org.activiti.engine.task.Task
import java.util.*

/**
 * 流程任务数据类
 *
 * @author K
 * @since 1.0.0
 */
data class FlowTask(
    /** 流程任务id，内部使用 */
    internal val _id: String,
    /** 流程任务名称 */
    val name: String,
    /** 流程定义key(bpmn文件中process元素的id) */
    val definitionKey: String,
    /** 流程任务受理人id */
    val assignee: String?
) {

    /** 父任务id，内部使用 */
    internal var _parentTaskId: String? = null
    /** 流程定义id，内部使用 */
    internal var _flowDefinitionId: String? = null
    /** 流程实例id，内部使用 */
    internal var _instanceId: String? = null
    /** 局部变量 */
    var localVariables: Map<String, Any>? = null
    /** 全局变量 */
    var flowVariables: Map<String, Any>? = null
    /** 任务原受理人id（有委托他人受理该任务） */
    var owner: String? = null
    /** 描述 */
    var description: String? = null
    /** 任务创建时间 */
    var createdTime: Date? = null
    /** 任务签收时间 */
    var claimedTime: Date? = null
    /** 任务过期时间 */
    var dueDate: Date? = null
    /** 优先级 */
    var priority = 0

    /**
     * 次构造器
     *
     * @param task activiti流程任务对象
     * @author K
     * @since 1.0.0
     */
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