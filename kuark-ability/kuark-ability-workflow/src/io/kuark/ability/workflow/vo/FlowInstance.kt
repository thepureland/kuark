package io.kuark.ability.workflow.vo

import io.kuark.ability.workflow.enums.FlowInstanceStatus
import org.activiti.engine.runtime.ProcessInstance
import java.util.*

/**
 * 流程实例数据类
 *
 * @author K
 * @since 1.0.0
 */
data class FlowInstance(
    internal val _id: String,
    var name: String,
    val bizKey: String,
    val definitionKey: String,
    val status: FlowInstanceStatus
) {

    private var _definitionId: String? = null
    private var startTime: Date? = null

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
