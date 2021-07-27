package io.kuark.ability.workflow.ibiz

import io.kuark.ability.workflow.vo.FlowInstance
import java.lang.IllegalArgumentException

/**
 * 流程实例相关业务接口
 *
 * @author K
 * @since 1.0.0
 */
interface IFlowInstanceBiz {

    /**
     * 获取流程实例
     *
     * @param bizKey 业务主键
     * @param definitionKey 流程定义key(bpmn文件中process元素的id)，为null将忽略该条件
     * @return 流程实例对象，找不到时返回null
     * @author K
     * @since 1.0.0
     */
    fun getFlowInstance(bizKey: String, definitionKey: String? = null): FlowInstance?

    /**
     * 获取同一定义key(即同一个bpmn文件)的所有流程实例
     *
     * @param definitionKey 流程定义key(bpmn文件中process元素的id)
     * @return List(流程实例对象)，找不到时返回空集合
     * @author K
     * @since 1.0.0
     */
    fun getFlowInstances(definitionKey: String): List<FlowInstance>

    /**
     * 启动流程实例
     *
     * @param definitionKey 流程定义key(bpmn文件中process元素的id)。当存在多个对应的流程定义时，取最新的一个。
     * @param bizKey 业务主键
     * @param instanceName 实例名称
     * @param variables 流程实例变量
     * @return 流程实例对象，启动失败时返回null
     * @author K
     * @since 1.0.0
     */
    fun startInstance(definitionKey: String, bizKey: String, instanceName: String, variables: Map<String, *>? = null): FlowInstance?

    /**
     * 激活流程实例，重复激活将忽略操作
     *
     * @param bizKey 业务主键
     * @param definitionKey 流程定义key(bpmn文件中process元素的id)，为null将忽略该条件。如果能确保bizKey的全局惟一性，可不传。
     * @throws IllegalArgumentException 当找不到对应流程实例时
     * @author K
     * @since 1.0.0
     */
    fun activateInstance(bizKey: String, definitionKey: String? = null)

    /**
     * 挂起流程实例，重复挂起将忽略操作
     *
     * @param bizKey 业务主键
     * @param definitionKey 流程定义key(bpmn文件中process元素的id)，为null将忽略该条件。如果能确保bizKey的全局惟一性，可不传。
     * @throws IllegalArgumentException 当找不到对应流程实例时
     * @author K
     * @since 1.0.0
     */
    fun suspendInstance(bizKey: String, definitionKey: String? = null)

    /**
     * 删除流程实例
     *
     * @param bizKey 业务主键
     * @param reason 原因
     * @param definitionKey 流程定义key(bpmn文件中process元素的id)，为null将忽略该条件。如果能确保bizKey的全局惟一性，可不传。
     * @throws IllegalArgumentException 当找不到对应流程实例时
     * @author K
     * @since 1.0.0
     */
    fun deleteInstance(bizKey: String, reason: String, definitionKey: String? = null)

    /**
     * 更新流程实例的业务主键
     *
     * @param oldBizKey 旧的业务主键
     * @param newBizKey 新的业务主键
     * @param definitionKey 流程定义key(bpmn文件中process元素的id)，为null将忽略该条件。如果能确保bizKey的全局惟一性，可不传。
     * @throws IllegalArgumentException 当指定的参数找不到对应流程实例时
     * @author K
     * @since 1.0.0
     */
    fun updateBizKey(oldBizKey: String, newBizKey: String, definitionKey: String? = null)

}