package io.kuark.ability.workflow.definition

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
    /** 流程定义id，内部使用 */
    internal val _id: String,
    /** 流程定义key(bpmn文件中process元素的id) */
    val key: String,
    /** 流程定义名称 */
    val name: String,
    /** 流程部署id，内部使用 */
    internal val _deploymentId: String
) {

    /** 描述  */
    var description: String? = null
    /** 流程定义的版本 */
    var version: Int? = null
    /** 是否被挂起 */
    var isSuspend: Boolean = false
    /** 部署时间 */
    var deploymentTime: Date? = null


    /**
     * 次构造器
     *
     * @param definition activiti流程定义对象
     * @author K
     * @since 1.0.0
     */
    constructor(definition: ProcessDefinition) : this(
        definition.id, definition.key, definition.name, definition.deploymentId
    ) {
        description = definition.description
        version = definition.version
        isSuspend = definition.isSuspended
    }

    /**
     * 次构造器
     *
     * @param deployment activiti流程部署对象
     * @param definition activiti流程定义对象
     * @author K
     * @since 1.0.0
     */
    constructor(deployment: Deployment, definition: ProcessDefinition) : this(definition) {
        deploymentTime = deployment.deploymentTime
    }

}
