package io.kuark.service.workflow.provider.model.vo

import org.activiti.engine.repository.Deployment
import org.activiti.engine.repository.Model
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
    /** 流程定义的版本 */
    var version: Int,
    /** 分类 */
    var category: String
) {

    /** 创建时间 */
    var createTime: Date? = null

    /** 最近更新时间 */
    var lastUpdateTime: Date? = null

    /** 是否已部署 */
    var deployed: Boolean = false

    /** 部署时间 */
    var deploymentTime: Date? = null

    /** 租户(所属系统)id */
    var tenantId: String? = null

    /** 是否被挂起 */
    var suspend: Boolean = false


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
    constructor(definition: ProcessDefinition, deployment: Deployment? = null) : this(
        definition.key,
        definition.name,
        definition.version,
        definition.category
    ) {
        _id = definition.id
        _deploymentId = definition.deploymentId
        _diagramResourceName = definition.diagramResourceName
        tenantId = definition.tenantId
        suspend = definition.isSuspended
        if (deployment != null) {
            deployed = true
            deploymentTime = deployment.deploymentTime
            lastUpdateTime = deploymentTime
        }
    }

    /**
     * 次构造器
     *
     * @param deployment activiti流程部署对象
     * @param modelId activiti流程模型id
     * @author K
     * @since 1.0.0
     */
    constructor(deployment: Deployment, modelId: String) : this(
        deployment.key,
        deployment.name,
        deployment.version,
        deployment.category
    ) {
        _modelId = modelId
        deploymentTime = deployment.deploymentTime
        lastUpdateTime = deploymentTime
        _deploymentId = deployment.id
        tenantId = deployment.tenantId
        deployed = true
    }

    /**
     * 次构造器
     *
     * @param model activiti流程部署对象
     * @param deployment activiti流程部署对象，默认为null
     * @author K
     * @since 1.0.0
     */
    constructor(model: Model, deployment: Deployment? = null) : this(
        model.key,
        model.name,
        model.version,
        model.category
    ) {
        _modelId = model.id
        _deploymentId = model.deploymentId
        tenantId = model.tenantId
        createTime = model.createTime
        lastUpdateTime = model.lastUpdateTime
        if (deployment != null) {
            deployed = true
            deploymentTime = deployment.deploymentTime
            lastUpdateTime = deploymentTime
        }
    }

}
