package io.kuark.ability.workflow.instance

import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.query.sort.Order
import org.activiti.bpmn.model.BpmnModel
import org.activiti.engine.history.HistoricActivityInstance
import org.activiti.engine.history.HistoricActivityInstanceQuery
import org.activiti.engine.history.HistoricProcessInstance
import org.activiti.engine.impl.RepositoryServiceImpl
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity
import java.io.InputStream
import java.util.stream.Collectors

/**
 * 流程实例相关业务接口
 *
 * @author K
 * @since 1.0.0
 */
interface IFlowInstanceBiz {

    /**
     * 检测流程实例是否存在
     *
     * @param bizKey 业务主键，不能为null
     * @param key 流程key(bpmn文件中process元素的id)，如果通过bizKey能惟一确定流程实例的话可以为null，默认为null
     * @param version 流程定义版本，如果为null则检测最新版本，默认为null。key不为空时生效
     * @return true: 流程实例存在，false: 流程实例不存在
     * @author K
     * @since 1.0.0
     */
    fun isExists(bizKey: String, key: String? = null, version: Int? = null): Boolean

    /**
     * 获取流程实例
     *
     * @param bizKey 业务主键，不能为null
     * @param key 流程key(bpmn文件中process元素的id)，如果通过bizKey能惟一确定流程实例的话可以为null，默认为null
     * @param version 流程定义版本，如果为null则返回最新版本，默认为null
     * @return 流程实例对象，找不到时返回null
     * @author K
     * @since 1.0.0
     */
    fun get(bizKey: String, key: String? = null, version: Int? = null): FlowInstance?

    /**
     * 查询流程实例
     *
     * @param queryItems 查询项，当查询项的属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
     * @param pageNum 分页页码，从1开始，默认为1，小于1将按1处理
     * @param pageSize 分页每页最大条数，默认为20，小于1将按不分页处理
     * @param orders 排序规则
     * @return List(流程实例对象)，找不到时返回空集合
     * @author K
     * @since 1.0.0
     */
    fun search(
        queryItems: FlowInstanceQueryItems,
        pageNum: Int = 1,
        pageSize: Int = 20,
        vararg orders: Order
    ): List<FlowInstance>

    /**
     * 激活流程实例，重复激活将忽略操作
     *
     * @param bizKey 业务主键，不能为空
     * @param key 流程key(bpmn文件中process元素的id)，如果通过bizKey能惟一确定流程实例的话可以为null，默认为null
     * @throws ObjectNotFoundException 当找不到对应流程实例时
     * @author K
     * @since 1.0.0
     */
    fun activate(bizKey: String, key: String? = null)

    /**
     * 挂起流程实例，重复挂起将忽略操作
     *
     * @param bizKey 业务主键，不能为空
     * @param key 流程key(bpmn文件中process元素的id)，如果通过bizKey能惟一确定流程实例的话可以为null，默认为null
     * @throws ObjectNotFoundException 当找不到对应流程实例时
     * @author K
     * @since 1.0.0
     */
    fun suspend(bizKey: String, key: String? = null)

    /**
     * 删除流程实例
     *
     * @param bizKey 业务主键，不能为null
     * @param key 流程key(bpmn文件中process元素的id)，如果通过bizKey能惟一确定流程实例的话可以为null，默认为null
     * @param reason 删除原因，可以为null，默认为null
     * @throws ObjectNotFoundException 当找不到对应流程实例时
     * @author K
     * @since 1.0.0
     */
    fun delete(bizKey: String, key: String? = null, reason: String? = null)

    /**
     * 更新流程实例的业务主键
     *
     * @param oldBizKey 旧的业务主键，不能为null
     * @param newBizKey 新的业务主键，不能为空
     * @param key 流程key(bpmn文件中process元素的id)，如果通过bizKey能惟一确定流程实例的话可以为null，默认为null
     * @throws IllegalArgumentException 新的业务主键为空或值非法时
     * @throws ObjectNotFoundException 当指定的参数找不到对应流程实例时
     * @author K
     * @since 1.0.0
     */
    fun updateBizKey(oldBizKey: String, newBizKey: String, key: String? = null)

    /**
     * 生成高亮流程图，已执行的节点高亮显示
     *
     * @param bizKey 业务主键，不能为null
     * @param key 流程key(bpmn文件中process元素的id)，如果通过bizKey能惟一确定流程实例的话可以为null，默认为null
     * @return 输入流
     * @throws IllegalArgumentException 业务主键为空
     * @author FRH
     * @author K
     * @since 1.0.0
     */
    fun genHighLightFlowDiagram(bizKey: String, key: String? = null): InputStream

}