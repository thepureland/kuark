package io.kuark.ability.workflow.ibiz

interface IFlowDefinition {

    fun deployWithBpmn(bpmnFileName: String, deploymentName: String, prefixPath: String = "bpmn")

}