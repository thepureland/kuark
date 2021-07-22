package io.kuark.ability.workflow.enums

import org.activiti.api.process.model.ProcessInstance

enum class FlowInstanceStatus {

    CREATED,
    RUNNING,
    SUSPENDED,
    CANCELLED,
    COMPLETED;

    companion object {
        fun enumOf(instanceStatus: ProcessInstance.ProcessInstanceStatus): FlowInstanceStatus {
            return when(instanceStatus) {
                ProcessInstance.ProcessInstanceStatus.CREATED -> CREATED
                ProcessInstance.ProcessInstanceStatus.RUNNING -> RUNNING
                ProcessInstance.ProcessInstanceStatus.SUSPENDED -> SUSPENDED
                ProcessInstance.ProcessInstanceStatus.CANCELLED -> CANCELLED
                ProcessInstance.ProcessInstanceStatus.COMPLETED -> COMPLETED
            }
        }
    }

}