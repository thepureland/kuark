package io.kuark.ability.workflow.task

import io.kuark.base.error.ObjectNotFoundException

/**
 * 流程任务相关业务接口
 *
 * @author K
 * @since 1.0.0
 */
interface IFlowTaskBiz {

    /**
     * 返回指定业务主键和任务定义key对应的流程任务
     *
     * @param bizKey 业务主键
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)
     * @return 流程任务，如果找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun getTask(bizKey: String, taskDefinitionKey: String): FlowTask?


    /**
     * 返回指定用户id的所有流程任务
     *
     * @param userIds 用户id可变数组
     * @return 指定用户所有流程任务，没有的话返回空列表
     * @throws IllegalArgumentException userIds为空时
     * @author K
     * @since 1.0.0
     */
    fun getTasksByUser(vararg userIds: String): List<FlowTask>

    /**
     * 用户签收(受理)流程任务
     *
     * @param bizKey 业务主键
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)
     * @param userId 用户id
     * @return true：签收成功，false：签收失败，已有其它用户签收
     * @throws IllegalArgumentException userId为空
     * @throws ObjectNotFoundException 找不到对应的任务时
     * @author K
     * @since 1.0.0
     */
    fun claimTask(bizKey: String, taskDefinitionKey: String, userId: String): Boolean

    /**
     * 取消签收(受理)流程任务
     *
     * @param bizKey 业务主键
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)
     * @return true：取消签收成功，false：取消签收失败，该任务原本就无用户签收
     * @throws ObjectNotFoundException 找不到对应的任务时
     * @author K
     * @since 1.0.0
     */
    fun unclaimTask(bizKey: String, taskDefinitionKey: String): Boolean

    /**
     * 将任务委托给指定用户，如果该任务未签收，则直接进行签收操作
     *
     * @param bizKey 业务主键
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)
     * @param userId 被委托的用户id
     * @return true: 委托成功，false：委托失败
     * @throws ObjectNotFoundException 找不到对应的任务时
     * @author K
     * @since 1.0.0
     */
    fun delegateTask(bizKey: String, taskDefinitionKey: String, userId: String): Boolean

    /**
     * 执行流程任务，只有任务签收(受理)人
     *
     * @param bizKey 业务主键
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)
     * @param userId 用户id
     * @param force 是否强制执行，默认为false
     * @return true: 成功，false：失败
     * @throws IllegalArgumentException userId为空
     * @throws ObjectNotFoundException 找不到对应的任务时
     * @author K
     * @since 1.0.0
     */
    fun completeTask(bizKey: String, taskDefinitionKey: String, userId: String, force: Boolean = false): Boolean

}