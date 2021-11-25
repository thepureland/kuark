package io.kuark.ability.workflow.provider.event

import org.activiti.engine.delegate.event.ActivitiEvent
import org.activiti.engine.delegate.event.ActivitiEventListener

/**
 * 工作流事件监听器接口
 *
 * @author K
 * @since 1.0.0
 */
interface IFlowEventListener : ActivitiEventListener {

    /**
     * 事件触发时被调用，可通过实现该方法来对各事件类型作出响应
     *
     * @param event 工作流事件
     * @author K
     * @since 1.0.0
     */
    fun onEvent(event: FlowEvent)

    /**
     * 当该事件监听器有异常抛出时，当前操作是否失败
     *
     * @return true：结束当前操作，false：当前操作继续
     * @author K
     * @since 1.0.0
     */
    override fun isFailOnException(): Boolean {
        return false
    }

    override fun onEvent(event: ActivitiEvent) {
        onEvent(FlowEvent(event))
    }

}