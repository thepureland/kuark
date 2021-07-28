package io.kuark.ability.workflow.event

import org.activiti.engine.delegate.event.ActivitiEvent
import org.activiti.engine.delegate.event.ActivitiEventListener

/**
 * 工作流全局事件监听器
 *
 * @author K
 * @since 1.0.0
 */
open class GlobalFlowEventListener(
    private val eventListeners: Map<FlowEventType, IFlowEventListener>
) : ActivitiEventListener {

    override fun onEvent(event: ActivitiEvent) {
        val eventType = FlowEventType.of(event.type)
        val eventListener = eventListeners[eventType]
        eventListener?.onEvent(FlowEvent(event))
    }

    override fun isFailOnException(): Boolean = false

}