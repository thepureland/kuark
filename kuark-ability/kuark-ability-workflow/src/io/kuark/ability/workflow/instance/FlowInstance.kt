package io.kuark.ability.workflow.instance

import org.activiti.engine.runtime.ProcessInstance
import java.util.*

/**
 * 流程实例数据类
 *
 * @author K
 * @since 1.0.0
 */
data class FlowInstance(
    /** 流程实例id，内部使用 */
    internal val _id: String,
    /** 流程实例名称 */
    var name: String,
    /** 业务主键 */
    val bizKey: String,
    /** 流程定义key(bpmn文件中process元素的id) */
    val definitionKey: String,
    /** 流程实例状态 */
    val status: FlowInstanceStatus
) {

    /** 流程定义id，内部使用 */
    private var _definitionId: String? = null
    /** 流程启动时间 */
    private var startTime: Date? = null

    /**
     * 次构造器
     *
     * @param instance activiti流程实例对象
     * @author K
     * @since 1.0.0
     */
    constructor(instance: ProcessInstance) : this(
        instance.id,
        instance.name ?: "",
        instance.businessKey,
        instance.processDefinitionKey,
        FlowInstanceStatus.of(instance)
    ) {
        this._definitionId = instance.processDefinitionId
        this.startTime = instance.startTime
    }

}
