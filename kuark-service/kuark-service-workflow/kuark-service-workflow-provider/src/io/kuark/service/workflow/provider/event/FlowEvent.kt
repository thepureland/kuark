package io.kuark.service.workflow.provider.event

import org.activiti.engine.delegate.event.ActivitiEvent

/**
 * 工作流事件
 *
 * @author K
 * @since 1.0.0
 */
open class FlowEvent(private val event: ActivitiEvent) {

    /** 事件类型枚举 */
    val type = io.kuark.service.workflow.provider.event.FlowEventType.Companion.of(event.type)

}