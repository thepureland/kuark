package io.kuark.ability.workflow.enums

import org.activiti.engine.runtime.ProcessInstance

/**
 * 流程实例状态枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class FlowInstanceStatus {

    /** 已创建 */
    CREATED,
    /** 运行中 */
    RUNNING,
    /** 挂起 */
    SUSPENDED,
    /** 完成 */
    COMPLETED;
    //    CANCELLED,


    companion object {
        fun of(internalProcessInstance: ProcessInstance): FlowInstanceStatus {
            return when {
                internalProcessInstance.isSuspended -> SUSPENDED
                internalProcessInstance.isEnded -> COMPLETED
                internalProcessInstance.startTime == null -> CREATED
                else -> RUNNING
            }
        }
    }

}