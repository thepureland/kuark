package io.kuark.ability.workflow.definition

import io.kuark.ability.workflow.event.IFlowEventListener
import io.kuark.ability.workflow.instance.FlowInstance
import io.kuark.base.error.ObjectAlreadyExistsException
import io.kuark.base.error.ObjectNotFoundException
import org.activiti.engine.ActivitiObjectNotFoundException

/**
 * 流程定义相关业务接口
 *
 * @author K
 * @since 1.0.0
 */
interface IFlowDefinitionBiz {

    /**
     * 检测流程定义是否存在
     *
     * @param key 流程key(bpmn文件中process元素的id)，不能为空
     * @param version 流程定义版本，如果为null则忽略该条件，默认为null
     * @return true: 流程定义存在，false: 流程定义不存在
     * @throws IllegalArgumentException key为空时
     * @author K
     * @since 1.0.0
     */
    fun isExists(key: String, version: Int? = null): Boolean

    /**
     * 获取流程定义
     *
     * @param key 流程key(bpmn文件中process元素的id)
     * @param version 流程定义版本，传null将取最新版本，默认为null
     * @return 流程定义对象，找不到返回null
     * @throws IllegalArgumentException key为空时
     * @author K
     * @since 1.0.0
     */
    fun get(key: String, version: Int? = null): FlowDefinition?

    /**
     * 查询流程定义
     *
     * @param criteria 查询条件对象，当对象的属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
     * @param pageNum 分页页码，从1开始，默认为1，小于1将按1处理
     * @param limit 分页每页最大条数，默认为20，小于1将按不分页处理
     * @author K
     * @since 1.0.0
     */
    fun search(criteria: FlowDefinitionCriteria, pageNum: Int = 1, limit: Int = 20): List<FlowDefinition>

    /**
     * 激活流程定义，重复激活将忽略操作
     *
     * @param key 流程key(bpmn文件中process元素的id)
     * @param version 流程定义版本，传null将取最新版本，默认为null
     * @throws IllegalArgumentException key为空时
     * @throws ObjectNotFoundException 当指定的key找不到对应流程定义时
     * @author K
     * @since 1.0.0
     */
    fun activate(key: String, version: Int? = null)

    /**
     * 挂起流程定义，重复挂起将忽略操作
     *
     * @param key 流程key(bpmn文件中process元素的id)
     * @param version 流程定义版本，传null将取最新版本，默认为null
     * @throws IllegalArgumentException key为空时
     * @throws ObjectNotFoundException 当指定的key找不到对应流程定义时
     * @author K
     * @since 1.0.0
     */
    fun suspend(key: String, version: Int? = null)

    /**
     * 删除指定流程key对应的所有流程的相关信息，只要一个失败，所有就删除失败
     *
     * @param key 流程key(bpmn文件中process元素的id)
     * @param version 流程定义版本，传null将取最新版本，默认为null
     * @param cascade 是否级联删除流程实例、历史流程实例和job, 默认为否
     * @throws IllegalArgumentException key为空时
     * @throws ObjectNotFoundException 当找不到对应流程定义时
     * @author K
     * @since 1.0.0
     */
    fun delete(key: String, version: Int? = null, cascade: Boolean = false)

    /**
     * 启动流程
     *
     * @param key 流程key(bpmn文件中process元素的id)。作为查询条件。不能为空
     * @param bizKey 业务主键。作为设置项，不能为空
     * @param instanceName 实例名称。作为设置项，不能为空
     * @param variables 流程实例变量，默认为null。作为设置项
     * @param eventListener 事件监听器，默认为null。作为设置项
     * @param version 流程定义版本，传null将取最新版本，默认为null。作为查询条件
     * @return 流程实例对象，启动失败时返回null
     * @throws IllegalArgumentException key或bizKey或instanceName为空时，或者bizKey值非法
     * @throws ObjectAlreadyExistsException 指定key、version、bizType的流程实例已经存在
     * @throws ObjectNotFoundException 找不到流程定义时
     * @author K
     * @since 1.0.0
     */
    fun start(
        key: String,
        bizKey: String,
        instanceName: String,
        variables: Map<String, *>? = null,
        eventListener: IFlowEventListener? = null,
        version: Int? = null
    ): FlowInstance?

}