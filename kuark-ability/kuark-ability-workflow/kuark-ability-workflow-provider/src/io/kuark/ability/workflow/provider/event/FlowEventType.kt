package io.kuark.ability.workflow.provider.event

import org.activiti.engine.delegate.event.ActivitiEventType

/**
 * 工作流事件类型枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class FlowEventType {

    /**
     * 创建了一个新实体。实体包含在事件中
     */
    ENTITY_CREATED,

    /**
     * 创建了一个新实体，初始化也完成了。如果这个实体的创建会包含子实体的创建，这个事件会在子实体都创建且初始化完成后被触发，这是与ENTITY_CREATED的区别。
     */
    ENTITY_INITIALIZED,

    /**
     * 更新了已存在的实体。实体包含在事件中。
     */
    ENTITY_UPDATED,

    /**
     * 删除了已存在的实体。实体包含在事件中。
     */
    ENTITY_DELETED,

    /**
     * 暂停了已存在的实体。实体包含在事件中。会被ProcessDefinitions, ProcessInstances 和 Tasks抛出。
     */
    ENTITY_SUSPENDED,

    /**
     * 激活了已存在的实体，实体包含在事件中。会被ProcessDefinitions, ProcessInstances 和 Tasks抛出。
     */
    ENTITY_ACTIVATED,

    /**
     * 定时器被调度。
     */
    TIMER_SCHEDULED,

    /**
     * 触发了定时器。job包含在事件中。
     */
    TIMER_FIRED,

    /**
     * 取消了一个作业。事件包含取消的作业。作业可以通过API调用取消，   任务完成后对应的边界定时器也会取消，在新流程定义发布时也会取消。
     */
    JOB_CANCELED,

    /**
     * 作业执行成功。job包含在事件中。
     */
    JOB_EXECUTION_SUCCESS,

    /**
     * 作业执行失败。作业和异常信息包含在事件中。
     */
    JOB_EXECUTION_FAILURE,

    /**
     * 因为作业执行失败，导致重试次数减少。作业包含在事件中。
     */
    JOB_RETRIES_DECREMENTED,

    /**
     * 自定义事件类型。这些类型的事件从不会被引擎抛出，仅作为外部api调用以便分派一个事件。
     */
    CUSTOM,

    /**
     * 监听器监听的流程引擎已经创建完毕，并准备好接受API调用。
     */
    ENGINE_CREATED,

    /**
     * 监听器监听的流程引擎已经关闭，不再接受API调用。
     */
    ENGINE_CLOSED,

    /**
     * 一个节点开始执行。该事件在一个节点正要开始执行前被分派。
     */
    ACTIVITY_STARTED,

    /**
     * 一个节点成功结束
     */
    ACTIVITY_COMPLETED,

    /**
     * 一个节点由于边界事件被取消
     */
    ACTIVITY_CANCELLED,

    /**
     * 一个节点收到了一个信号。事件在节点响应信号后被分派。
     */
    ACTIVITY_SIGNALED,

    /**
     * 一个节点将要被补偿。事件包含了将要执行补偿的节点id。
     */
    ACTIVITY_COMPENSATE,

    /**
     * 消息已通过消息中介抛出或消息结束事件发送
     */
    ACTIVITY_MESSAGE_SENT,

    /**
     * 一个边界、中间或子流程开始消息捕获事件已经启动并且在等待消息
     */
    ACTIVITY_MESSAGE_WAITING,

    /**
     * 一个节点收到了一个消息。在节点收到消息之前触发。收到后，会触发ACTIVITY_SIGNAL或ACTIVITY_STARTED，这会根据节点的类型（边界事件，事件子流程开始事件）
     */
    ACTIVITY_MESSAGE_RECEIVED,

    /**
     * 一个节点收到了一个错误事件。在节点实际处理错误之前触发。   事件的activityId对应着处理错误的节点。 这个事件后续会是ACTIVITY_SIGNALLED或ACTIVITY_COMPLETE， 如果错误发送成功的话。
     */
    ACTIVITY_ERROR_RECEIVED,

    /**
     * {@link HistoricActivityInstance}被创建。
     * 这是一个 {@link ActivitiEventType#ENTITY_CREATED} 和 {@link ActivitiEventType#ENTITY_INITIALIZED}的特殊版本事件,
     * 使用方式和 {@link ActivitiEventType#ACTIVITY_STARTED} 一样, 但包含着稍微不同的数据。
     *
     * 注意：当实体为{@link HistoricActivityInstance}时，它将是一个 {@link ActivitiEntityEvent} 事件
     *
     * 注意：历史 (最低级别的 ACTIVITY) 必须要能够接收该事件。
     */
    HISTORIC_ACTIVITY_INSTANCE_CREATED,

    /**
     * {@link HistoricActivityInstance} 被标记为结束。
     * 这是一个 {@link ActivitiEventType#ENTITY_UPDATED} 的特殊版本事件,
     * 使用方式和 {@link ActivitiEventType#ACTIVITY_COMPLETED} 一样, 但包含着稍微不同的数据。 (如： 结束时间, 持续时长, 等等)。
     *
     * 注意：历史 (最低级别的 ACTIVITY) 必须要能够接收该事件。
     */
    HISTORIC_ACTIVITY_INSTANCE_ENDED,

    /**
     * 引擎已持有从源节点到目标节点的序列流
     */
    SEQUENCEFLOW_TAKEN,

    /**
     * 抛出了未捕获的BPMN错误。流程没有提供针对这个错误的处理器。   事件的activityId为空。
     */
    UNCAUGHT_BPMN_ERROR,

    /**
     * 创建了一个变量。事件包含变量名，变量值和对应的分支或任务（如果存在）。
     */
    VARIABLE_CREATED,

    /**
     * 更新了一个变量。事件包含变量名，变量值和对应的分支或任务（如果存在）。
     */
    VARIABLE_UPDATED,

    /**
     * 删除了一个变量。事件包含变量名，变量值和对应的分支或任务（如果存在）。
     */
    VARIABLE_DELETED,

    /**
     * 创建了新任务。它位于ENTITY_CREATE事件之后。当任务是由流程创建时，     这个事件会在TaskListener执行之前被执行。
     */
    TASK_CREATED,

    /**
     * 任务被分配给了一个人员。事件包含任务。
     */
    TASK_ASSIGNED,

    /**
     * 任务被完成了。它会在ENTITY_DELETE事件之前触发。当任务是流程一部分时，事件会在流程继续运行之前，   后续事件将是ACTIVITY_COMPLETE，对应着完成任务的节点。
     */
    TASK_COMPLETED,

    /**
     * 流程实例已启动。该事件在ENTITY_INITIALIZED事件后被调度。
     */
    PROCESS_STARTED,

    /**
     * 流程已结束。在最后一个节点的ACTIVITY_COMPLETED事件之后触发。 当流程到达的状态，没有任何后续连线时， 流程就会结束。
     */
    PROCESS_COMPLETED,

    /**
     * 流程已结束，但伴随着一个错误结束事件
     */
    PROCESS_COMPLETED_WITH_ERROR_END_EVENT,

    /**
     * 流程已被取消。在流程实例被通过
     * @see io.kuark.ability.workflow.instance.FlowInstanceBiz#deleteInstance(String, String, String)删除时, 但在数据库删除之前。
     */
    PROCESS_CANCELLED,

    /**
     * {@link HistoricProcessInstance}被创建.
     * 这是一个 {@link ActivitiEventType#ENTITY_CREATED} 和 {@link ActivitiEventType#ENTITY_INITIALIZED} 的特殊版本事件,
     * 使用方式和 {@link ActivitiEventType#PROCESS_STARTED} 一样, 但包含着稍微不同的数据 (如： 开始时间, 开始用户id, 等等).
     *
     * 注意：当实体为{@link HistoricActivityInstance}时，它将是一个 {@link ActivitiEntityEvent} 事件
     *
     * 注意：历史 (最低级别的 ACTIVITY) 必须要能够接收该事件。
     */
    HISTORIC_PROCESS_INSTANCE_CREATED,

    /**
     * {@link HistoricProcessInstance} 被标记为结束。
     * 这是一个 {@link ActivitiEventType#ENTITY_UPDATED} 的特殊版本事件,
     * 使用方式和 {@link ActivitiEventType#PROCESS_COMPLETED} 一样, 但包含着稍微不同的数据。 (如： 结束时间, 持续时长, 等等)
     *
     * 注意：历史 (最低级别的 ACTIVITY) 必须要能够接收该事件。
     */
    HISTORIC_PROCESS_INSTANCE_ENDED,

    /**
     * 用户被添加到一个组里。事件包含了用户和组的id。
     */
    MEMBERSHIP_CREATED,

    /**
     * 用户被从一个组中删除。事件包含了用户和组的id。
     */
    MEMBERSHIP_DELETED,

    /**
     * 所有成员被从一个组中删除。在成员删除之前触发这个事件，所以他们都是可以访问的。   因为性能方面的考虑，不会为每个成员触发单独的MEMBERSHIP_DELETED事件。
     */
    MEMBERSHIPS_DELETED;


    companion object {
        fun of(activitiEventType: ActivitiEventType): FlowEventType {
            return when (activitiEventType) {
                ActivitiEventType.ENTITY_CREATED -> ENTITY_CREATED
                ActivitiEventType.ENTITY_INITIALIZED -> ENTITY_INITIALIZED
                ActivitiEventType.ENTITY_UPDATED -> ENTITY_UPDATED
                ActivitiEventType.ENTITY_DELETED -> ENTITY_DELETED
                ActivitiEventType.ENTITY_SUSPENDED -> ENTITY_SUSPENDED
                ActivitiEventType.ENTITY_ACTIVATED -> ENTITY_ACTIVATED
                ActivitiEventType.TIMER_SCHEDULED -> TIMER_SCHEDULED
                ActivitiEventType.TIMER_FIRED -> TIMER_FIRED
                ActivitiEventType.JOB_CANCELED -> JOB_CANCELED
                ActivitiEventType.JOB_EXECUTION_SUCCESS -> JOB_EXECUTION_SUCCESS
                ActivitiEventType.JOB_EXECUTION_FAILURE -> JOB_EXECUTION_FAILURE
                ActivitiEventType.JOB_RETRIES_DECREMENTED -> JOB_RETRIES_DECREMENTED
                ActivitiEventType.CUSTOM -> CUSTOM
                ActivitiEventType.ENGINE_CREATED -> ENGINE_CREATED
                ActivitiEventType.ENGINE_CLOSED -> ENGINE_CLOSED
                ActivitiEventType.ACTIVITY_STARTED -> ACTIVITY_STARTED
                ActivitiEventType.ACTIVITY_COMPLETED -> ACTIVITY_COMPLETED
                ActivitiEventType.ACTIVITY_CANCELLED -> ACTIVITY_CANCELLED
                ActivitiEventType.ACTIVITY_SIGNALED -> ACTIVITY_SIGNALED
                ActivitiEventType.ACTIVITY_COMPENSATE -> ACTIVITY_COMPENSATE
                ActivitiEventType.ACTIVITY_MESSAGE_SENT -> ACTIVITY_MESSAGE_SENT
                ActivitiEventType.ACTIVITY_MESSAGE_WAITING -> ACTIVITY_MESSAGE_WAITING
                ActivitiEventType.ACTIVITY_MESSAGE_RECEIVED -> ACTIVITY_MESSAGE_RECEIVED
                ActivitiEventType.ACTIVITY_ERROR_RECEIVED -> ACTIVITY_ERROR_RECEIVED
                ActivitiEventType.HISTORIC_ACTIVITY_INSTANCE_CREATED -> HISTORIC_ACTIVITY_INSTANCE_CREATED
                ActivitiEventType.HISTORIC_ACTIVITY_INSTANCE_ENDED -> HISTORIC_ACTIVITY_INSTANCE_ENDED
                ActivitiEventType.SEQUENCEFLOW_TAKEN -> SEQUENCEFLOW_TAKEN
                ActivitiEventType.UNCAUGHT_BPMN_ERROR -> UNCAUGHT_BPMN_ERROR
                ActivitiEventType.VARIABLE_CREATED -> VARIABLE_CREATED
                ActivitiEventType.VARIABLE_UPDATED -> VARIABLE_UPDATED
                ActivitiEventType.VARIABLE_DELETED -> VARIABLE_DELETED
                ActivitiEventType.TASK_CREATED -> TASK_CREATED
                ActivitiEventType.TASK_ASSIGNED -> TASK_ASSIGNED
                ActivitiEventType.TASK_COMPLETED -> TASK_COMPLETED
                ActivitiEventType.PROCESS_STARTED -> PROCESS_STARTED
                ActivitiEventType.PROCESS_COMPLETED -> PROCESS_COMPLETED
                ActivitiEventType.PROCESS_COMPLETED_WITH_ERROR_END_EVENT -> PROCESS_COMPLETED_WITH_ERROR_END_EVENT
                ActivitiEventType.PROCESS_CANCELLED -> PROCESS_CANCELLED
                ActivitiEventType.HISTORIC_PROCESS_INSTANCE_CREATED -> HISTORIC_PROCESS_INSTANCE_CREATED
                ActivitiEventType.HISTORIC_PROCESS_INSTANCE_ENDED -> HISTORIC_PROCESS_INSTANCE_ENDED
                ActivitiEventType.MEMBERSHIP_CREATED -> MEMBERSHIP_CREATED
                ActivitiEventType.MEMBERSHIP_DELETED -> MEMBERSHIP_DELETED
                ActivitiEventType.MEMBERSHIPS_DELETED -> MEMBERSHIPS_DELETED
                else -> error("未支持的工作流事件类型：${activitiEventType.name}")
            }
        }

    }

}