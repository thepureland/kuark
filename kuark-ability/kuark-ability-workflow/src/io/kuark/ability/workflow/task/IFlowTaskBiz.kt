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
     * 检测任务是否存在
     *
     * @param bizKey 业务主键，不能为null
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)，不能为null
     * @return true：任务存在，false：任务不存在
     * @author K
     * @since 1.0.0
     */
    fun isExists(bizKey: String, taskDefinitionKey: String): Boolean

    /**
     * 返回指定业务主键和任务定义key对应的流程任务
     *
     * @param bizKey 业务主键，不能为null
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)，不能为null
     * @return 流程任务，如果找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun get(bizKey: String, taskDefinitionKey: String): FlowTask?


    /**
     * 查询任务
     *
     * @param queryItems 查询项，当查询项的属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
     * @return 指定用户所有流程任务，没有的话返回空列表
     * @param pageNum 分页页码，从1开始，默认为1，小于1将按1处理
     * @param limit 分页每页最大条数，默认为20，小于1将按不分页处理
     * @return List(流程任务对象)，找不到时返回空集合
     * @author K
     * @since 1.0.0
     */
    fun search(queryItems: FlowTaskQueryItems, pageNum: Int = 1, limit: Int = 20): List<FlowTask>

    /**
     * 用户签收(受理)流程任务
     *
     * @param bizKey 业务主键，不能为null
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)，不能为null
     * @param assignee 签收(受理)用户id，不能为空
     * @return true：签收成功，false：签收失败，已有其它用户签收
     * @throws IllegalArgumentException assignee为空
     * @throws ObjectNotFoundException 找不到对应的任务时
     * @author K
     * @since 1.0.0
     */
    fun claim(bizKey: String, taskDefinitionKey: String, assignee: String): Boolean

    /**
     * 取消签收(受理)流程任务
     *
     * @param bizKey 业务主键，不能为null
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)，不能为null
     * @return true：取消签收成功，false：取消签收失败，该任务原本就无用户签收
     * @throws ObjectNotFoundException 找不到对应的任务时
     * @author K
     * @since 1.0.0
     */
    fun unclaim(bizKey: String, taskDefinitionKey: String): Boolean

    /**
     * 将任务委托给指定用户，如果该任务未签收，则直接进行签收操作
     *
     * @param bizKey 业务主键，不能为null
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)，不能为null
     * @param assignee 被委托的用户id，不能为空
     * @return true: 委托成功，false：委托失败
     * @throws IllegalArgumentException assignee为空
     * @throws ObjectNotFoundException 找不到对应的任务时
     * @author K
     * @since 1.0.0
     */
    fun delegate(bizKey: String, taskDefinitionKey: String, assignee: String): Boolean

    /**
     * 执行流程任务。不强制执行时，只有任务签收(受理)人才能执行任务；强制执行时，任何用户都可以执行该任务。
     *
     * @param bizKey 业务主键，不能为null
     * @param taskDefinitionKey 任务定义key(bpmn文件userTask元素的id)，不能为null
     * @param userId 用户id，不能为空
     * @param force 是否强制执行，默认为false
     * @return true: 成功，false：失败
     * @throws IllegalArgumentException userId为空
     * @throws ObjectNotFoundException 找不到对应的任务时
     * @author K
     * @since 1.0.0
     */
    fun complete(bizKey: String, taskDefinitionKey: String, userId: String, force: Boolean = false): Boolean

}