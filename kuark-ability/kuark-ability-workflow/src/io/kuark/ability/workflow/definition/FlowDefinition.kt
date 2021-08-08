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
    /** 流程定义key(bpmn文件中process元素的id) */
    val key: String,
    /** 流程定义名称 */
    val name: String,
) {

    /** 描述  */
    var description: String? = null

    /** 流程定义的版本 */
    var version: Int? = null

    /** 是否被挂起 */
    var isSuspend: Boolean = false

    /** 部署时间 */
    var deploymentTime: Date? = null


    /** 流程定义id，内部使用 */
    internal var _id: String? = null
    /** 流程模型id，内部使用 */
    internal var _modelId: String? = null
    /** 流程部署id，内部使用 */
    internal var _deploymentId: String? = null
    /** 流程图资源名称，内部使用 */
    internal var _diagramResourceName: String? = null


    /**
     * 次构造器
     *
     * @param definition activiti流程定义对象
     * @param deployment activiti流程部署对象，默认为null
     * @author K
     * @since 1.0.0
     */
    constructor(definition: ProcessDefinition, deployment: Deployment? = null) : this(definition.key, definition.name) {
        _id = definition.id
        _deploymentId = definition.deploymentId
        _diagramResourceName = definition.diagramResourceName
        description = definition.description
        version = definition.version
        isSuspend = definition.isSuspended
        if (deployment != null) {
            deploymentTime = deployment.deploymentTime
        }
    }

    /**
     * 次构造器
     *
     * @param deployment activiti流程部署对象
     * @author K
     * @since 1.0.0
     */
    constructor(deployment: Deployment, modelId: String) : this(deployment.key, deployment.name) {
        _modelId = modelId
        deploymentTime = deployment.deploymentTime
        _deploymentId = deployment.id
        version = deployment.version
    }

}
