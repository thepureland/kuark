package io.kuark.ability.workflow.provider.model.vo

import org.activiti.engine.runtime.ProcessInstance
import java.util.*

/**
 * 流程实例数据类
 *
 * @author K
 * @since 1.0.0
 */
data class FlowInstance(
    /** 流程实例id，内部使用 */
    internal val _id: String,
    /** 流程实例名称 */
    var name: String,
    /** 业务主键 */
    val bizKey: String,
    /** 流程定义key(bpmn文件中process元素的id) */
    val definitionKey: String,
    /** 是否挂起 */
    val suspend: Boolean = false
) {

    /** 流程定义id，内部使用 */
    internal var _definitionId: String? = null
    /** 发起时间 */
    var startTime: Date? = null
    /** 发起者id */
    var startUserId: String? = null
    /** 流程定义名称 */
    var definitionName: String? = null
    /** 流程定义版本 */
    var definitionVersion: Int? = null

    /**
     * 次构造器
     *
     * @param instance activiti流程实例对象
     * @author K
     * @since 1.0.0
     */
    constructor(instance: ProcessInstance) : this(
        instance.id,
        instance.name ?: "",
        instance.businessKey,
        instance.processDefinitionKey,
        instance.isSuspended
    ) {
        _definitionId = instance.processDefinitionId
        startTime = instance.startTime
        startUserId = instance.startUserId
        definitionName = instance.processDefinitionName
        definitionVersion = instance.processDefinitionVersion
    }

}
