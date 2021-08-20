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
    val flowDefinitionKey: String,
    /** 业务主键 */
    val bizKey: String,
    /** 流程任务受理人id */
    val assignee: String?,
) {

    /** 父任务id，内部使用 */
    internal var _parentTaskId: String? = null

    /** 流程定义id，内部使用 */
    internal var _flowDefinitionId: String? = null

    /** 流程实例id，内部使用 */
    internal var _instanceId: String? = null

    /** 执行id，内部使用 */
    internal var _executionId: String? = null

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
    var priority: Int = 0

    /** 表单key */
    var formKey: String? = null

    var appVersion: Int? = null

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
        task.businessKey,
        task.assignee
    ) {
        _flowDefinitionId = task.processDefinitionId
        _instanceId = task.processInstanceId
        localVariables = task.taskLocalVariables
        flowVariables = task.processVariables
        owner = task.owner
        description = task.description
        createdTime = task.createTime
        claimedTime = task.claimTime
        dueDate = task.dueDate
        priority = task.priority
        _parentTaskId = task.parentTaskId
        _executionId = task.executionId
        priority = task.priority
        formKey = task.formKey
        appVersion = task.appVersion
    }

}