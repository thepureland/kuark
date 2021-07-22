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
     * @return 流程实例对象，找不到时返回null
     * @author K
     * @since 1.0.0
     */
    fun getFlowInstance(bizKey: String): FlowInstance?

    /**
     * 获取流程实例
     *
     * @param definitionKey 流程定义key
     * @return List(流程实例对象)，找不到时返回空集合
     * @author K
     * @since 1.0.0
     */
    fun getFlowInstances(definitionKey: String): List<FlowInstance>

    /**
     * 启动流程实例
     *
     * @param definitionKey 流程定义key。当存在多个对应的流程定义时，取最新的一个。
     * @param bizKey 业务主键
     * @param variables 流程实例变量
     * @return 流程实例对象，启动失败时返回null
     * @author K
     * @since 1.0.0
     */
    fun startInstance(definitionKey: String, bizKey: String, variables: Map<String, *>? = null): FlowInstance?

    /**
     * 激活流程实例，重复激活将忽略操作
     *
     * @param instanceId 流程实例id
     * @throws IllegalArgumentException 当指定的instanceId找不到对应流程定义时
     * @author K
     * @since 1.0.0
     */
    fun activateInstance(instanceId: String)

    /**
     * 挂起流程实例，重复挂起将忽略操作
     *
     * @param instanceId 流程实例id
     * @throws IllegalArgumentException 当指定的instanceId找不到对应流程定义时
     * @author K
     * @since 1.0.0
     */
    fun suspendInstance(instanceId: String)

    /**
     * 更新流程实例的业务主键
     *
     * @param instanceId 流程实例id
     * @param bizKey 流程实例的业务主键
     * @throws IllegalArgumentException 当指定的参数找不到对应流程实例时
     * @author K
     * @since 1.0.0
     */
    fun updateBizKey(instanceId: String, bizKey: String)


}