package io.kuark.ability.workflow.model

import org.activiti.engine.repository.Model
import java.util.*

/**
 * 流程模型
 *
 * @author K
 * @since 1.0.0
 */
data class FlowModel(
    /** 流程key(bpmn文件中process元素的id) */
    var key: String,
    /** 流程名称 */
    var name: String,
    /** 模型版本 */
    var version: Int
) {

    /** 创建时间 */
    var createTime: Date? = null

    /** 最近更新时间 */
    var lastUpdateTime: Date? = null

    /** 是否已部署 */
    var isDeployed: Boolean = false

    /** 分类 */
    var category: String? = null

    /** 租户(所属系统)id */
    var tenantId: String? = null


    /** 模型id，内部使用 */
    internal var _id: String? = null

    /** 部署id，内部使用 */
    internal var _deploymentId: String? = null


    /**
     * 次构造器
     *
     * @param model activiti流程模型对象
     * @author K
     * @since 1.0.0
     */
    constructor(model: Model) : this(model.key, model.name, model.version) {
        createTime = model.createTime
        lastUpdateTime = model.lastUpdateTime
        _id = model.id
        _deploymentId = model.deploymentId
        isDeployed = _deploymentId != null && _deploymentId!!.isNotBlank()
        category = model.category
        tenantId = model.tenantId
    }

}