package io.kuark.ability.workflow.event

import org.activiti.engine.delegate.event.ActivitiEvent

/**
 * 工作流事件
 *
 * @author K
 * @since 1.0.0
 */
open class FlowEvent(private val event: ActivitiEvent) {

    /** 事件类型枚举 */
    val type: FlowEventType = FlowEventType.of(event.type)

    // 事件
    // ActivitiEventListener
    // BaseEntityEventListener
    // 注册eventListeners（先）或typedEventListeners
    // 运行阶段添加的监听器引擎重启后就消失
    // ActivitiTestCase

}