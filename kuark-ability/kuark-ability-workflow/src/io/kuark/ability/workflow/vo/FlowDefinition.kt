package io.kuark.ability.workflow.vo

import org.activiti.engine.repository.Deployment
import org.activiti.engine.repository.ProcessDefinition
import java.util.*

/**
 * 流程定义数据类
 *
 * @author K
 * @since 1.0.0
 */
data class FlowDefinition(
    internal val _id: String,
    val key: String,
    val name: String,
    internal val _deploymentId: String
) {

    private var description: String? = null
    private var version: Int? = null
    private var isSuspend: Boolean = false
    private var deploymentTime: Date? = null


    constructor(definition: ProcessDefinition) : this(
        definition.id, definition.key, definition.name, definition.deploymentId
    ) {
        description = definition.description
        version = definition.version
        isSuspend = definition.isSuspended
    }

    constructor(deployment: Deployment, definition: ProcessDefinition) : this(definition) {
        deploymentTime = deployment.deploymentTime
    }

}
