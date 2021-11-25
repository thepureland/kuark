package io.kuark.ability.workflow.provider.ibiz

import io.kuark.ability.workflow.provider.model.vo.FlowDefinition
import io.kuark.ability.workflow.common.definition.FlowDefinitionSearchParams
import io.kuark.ability.workflow.provider.event.IFlowEventListener
import io.kuark.ability.workflow.provider.model.vo.FlowInstance
import io.kuark.base.error.ObjectAlreadyExistsException
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.io.PathKit
import io.kuark.base.query.sort.Order
import java.io.InputStream

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
     * @param key 流程定义key(bpmn文件中process元素的id)，不能为空
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
     * @param key 流程定义key(bpmn文件中process元素的id)
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
     * @param searchItems 查询参数，当查询参数的属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
     * @param pageNum 分页页码，从1开始，默认为1，小于1将按1处理
     * @param pageSize 分页每页最大条数，默认为20，小于1将按不分页处理
     * @param orders 排序规则
     * @return List(流程定义对象)，找不到时返回空集合
     * @author K
     * @since 1.0.0
     */
    fun search(
        searchItems: FlowDefinitionSearchParams,
        pageNum: Int = 1,
        pageSize: Int = 20,
        vararg orders: Order,
    ): List<FlowDefinition>

    /**
     * 创建流程定义
     *
     * @param key 流程定义key(bpmn文件中process元素的id)，不能为空
     * @param name 流程名称，不能为空
     * @param category 分类，不能为空
     * @param flowJson 流程的json内容，不能为空
     * @param svgXml 流程图(svg格式)的xml内容，不能为空
     * @param tenantId 租户(所属系统)id，可以为null，默认为null
     * @return 流程定义对象
     * @throws IllegalArgumentException 任意参数为空时，具体哪个参数参照异常消息
     * @throws ObjectAlreadyExistsException 流程定义key重复时
     * @author K
     * @since 1.0.0
     */
    fun create(
        key: String, name: String, category: String, flowJson: String, svgXml: String, tenantId: String? = null
    ): FlowDefinition

    /**
     * 更新流程定义。
     *
     * 如果定义已部署，会自动新增一条新版本的定义记录，而不是更新原来的的定义记录；若未部署，则是对原记录作更新操作！！！
     *
     * @param key 流程定义key(bpmn文件中process元素的id)。作为查询条件
     * @param version 定义版本，如果为null则查询最新的版本，默认为null。作为查询条件
     * @param name 流程名称，作为更新项，如果为空将忽略该项的更新。
     * @param category 分类，作为更新项，如果为空将忽略该项的更新。
     * @param flowJson 流程的json内容，作为更新项，如果为空将忽略该项的更新。
     * @param svgXml 流程图(svg格式)的xml内容，作为更新项，如果为空将忽略该项的更新。
     * @param tenantId 租户(所属系统)id。作为更新项，如果为空将忽略该项的更新。
     * @return 流程定义对象
     * @throws IllegalArgumentException 流程定义key为空时
     * @throws ObjectNotFoundException 找不到流程定义时
     * @author K
     * @since 1.0.0
     */
    fun update(
        key: String,
        version: Int? = null,
        name: String?,
        category: String?,
        flowJson: String?,
        svgXml: String?,
        tenantId: String? = null
    ): FlowDefinition

    /**
     * 部署流程，通过已经存在的流程定义。一个版本的定义限制只能部署一次！
     *
     * @param key 流程定义key(bpmn文件中process元素的id)，不能为空
     * @param version 定义版本，如果为null则使用最新的版本，默认为null
     * @return 流程定义对象
     * @throws IllegalStateException 流程定义重复部署时
     * @throws IllegalArgumentException 流程定义key为空时
     * @throws ObjectNotFoundException 找不到流程定义时
     * @author K
     * @since 1.0.0
     */
    fun deploy(key: String, version: Int? = null): FlowDefinition

    /**
     * 部署流程，通过指定的bpmn文件相关信息
     *
     * @param name 流程名称，不能为空
     * @param bpmnFileName bpmn文件名，不传后缀(.bpmn)时将自动拼接
     * @param diagramFileName bpmn对应的流程图文件名，不传后缀(.png)时将自动拼接。可选，不指定时将不会部署流程图。
     * @param prefixPath 目录前缀，默认为bpmn。bpmn和流程图文件都必须放置于该目录下
     * @return 流程定义对象
     * @throws ObjectNotFoundException 文件找不到时
     * @author K
     * @since 1.0.0
     */
    fun deployWithBpmn(
        name: String, bpmnFileName: String, diagramFileName: String? = null, prefixPath: String = "bpmn"
    ): FlowDefinition

    /**
     * 部署多个流程，通过zip压缩包。包中可包含bpmn和流程图文件,文件名(不含后缀)一样视为同一流程，流程图为可选。
     *
     * @param deploymentName 部署名称
     * @param zipFileName 压缩包文件名，不传后缀(.zip)时将自动拼接。
     * @param prefixPath 目录前缀，默认为bpmn。zip包必须放置于该目录下
     * @return List(流程定义对象)
     * @throws ObjectNotFoundException 文件找不到时
     * @author K
     * @since 1.0.0
     */
    fun deployWithZip(
        deploymentName: String,
        zipFileName: String,
        prefixPath: String = PathKit.getResourcePath("bpmn")
    ): List<FlowDefinition>

    /**
     * 获取流程图
     *
     * @param key 流程定义key(bpmn文件中process元素的id)。当有多个流程定义时，取第一个！
     * @param version 流程定义版本，传null将取最新版本，默认为null
     * @return 图片输入流对象，流程定义不存在时返回null
     * @author K
     * @since 1.0.0
     */
    fun getFlowDiagram(key: String, version: Int? = null): InputStream?

    /**
     * 生成流程图(可以在部署前)
     *
     * @param bpmnFileName bpmn文件名，不传后缀(.bpmn)时将自动拼接
     * @param prefixPath 目录前缀，默认为bpmn。bpmn文件必须放置于该目录下
     * @return 图片输入流对象
     * @throws ObjectNotFoundException 文件找不到时
     * @author K
     * @since 1.0.0
     */
    fun genFlowDiagram(bpmnFileName: String, prefixPath: String = PathKit.getResourcePath("bpmn")): InputStream

    /**
     * 激活流程定义，重复激活将忽略操作
     *
     * @param key 流程定义key(bpmn文件中process元素的id)
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
     * @param key 流程定义key(bpmn文件中process元素的id)
     * @param version 流程定义版本，传null将取最新版本，默认为null
     * @throws IllegalArgumentException key为空时
     * @throws ObjectNotFoundException 当指定的key找不到对应流程定义时
     * @author K
     * @since 1.0.0
     */
    fun suspend(key: String, version: Int? = null)

    /**
     * 删除指定流程定义key对应的所有流程的相关信息，只要一个失败，所有就删除失败
     *
     * @param key 流程定义key(bpmn文件中process元素的id)
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
     * @param key 流程定义key(bpmn文件中process元素的id)。作为查询条件。不能为空
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