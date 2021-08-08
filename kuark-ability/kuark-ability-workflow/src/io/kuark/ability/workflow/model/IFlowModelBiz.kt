package io.kuark.ability.workflow.model

import io.kuark.ability.workflow.definition.FlowDefinition
import io.kuark.base.error.ObjectAlreadyExistsException
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.io.PathKit
import java.io.InputStream

/**
 * 流程模型相关业务接口
 *
 * @author K
 * @since 1.0.0
 */
interface IFlowModelBiz {

    /**
     * 检测模型是否存在
     *
     * @param key 流程key(bpmn文件中process元素的id)，不能为空
     * @param version 模型版本，如果为null则忽略该条件，默认为null
     * @return true: 模型存在，false: 模型不存在
     * @throws IllegalArgumentException key为空时
     * @author K
     * @since 1.0.0
     */
    fun isExists(key: String, version: Int? = null): Boolean

    /**
     * 返回指定条件的模型
     *
     * @param key 流程key(bpmn文件中process元素的id)，不能为空
     * @param version 模型版本，如果为null返回最新的版本，默认为null
     * @throws IllegalArgumentException key为空时
     * @return 流程模型对象，找不到时返回null
     * @author K
     * @since 1.0.0
     */
    fun get(key: String, version: Int? = null): FlowModel?

    /**
     * 查询流程模型
     *
     * @param criteria 查询条件对象，当对象的属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
     * @param pageNum 分页页码，从1开始，默认为1，小于1将按1处理
     * @param limit 分页每页最大条数，默认为20，小于1将按不分页处理
     * @author K
     * @since 1.0.0
     */
    fun search(criteria: FlowModelCriteria, pageNum: Int = 1, limit: Int = 20): List<FlowModel>

    /**
     * 创建流程模型
     *
     * @param key 流程key(bpmn文件中process元素的id)，不能为空
     * @param name 流程名称，不能为空
     * @param flowJson 流程的json内容，不能为空
     * @param svgXml 流程图(svg格式)的xml内容，不能为空
     * @return 流程模型对象
     * @throws IllegalArgumentException 任意参数为空时，具体哪个参数参照异常消息
     * @throws ObjectAlreadyExistsException 流程key重复时
     * @author K
     * @since 1.0.0
     */
    fun create(key: String, name: String, flowJson: String, svgXml: String): FlowModel

    /**
     * 更新流程模型。
     *
     * 如果模型已部署，会自动新增一条新版本的模型记录，而不是更新原来的的模型记录；若未部署，则是对原记录作更新操作！！！
     *
     * @param key 流程key(bpmn文件中process元素的id)。作为查询条件
     * @param version 模型版本，如果为null则查询最新的版本，默认为null。作为查询条件
     * @param name 流程名称，如果为空将忽略该项的更新。作为更新项
     * @param svgXml 流程图(svg格式)的xml内容，如果为空将忽略该项的更新。作为更新项
     * @param flowJson 流程的json内容，如果为空将忽略该项的更新。作为更新项
     * @return 流程模型对象
     * @throws IllegalArgumentException 流程key为空时
     * @throws ObjectNotFoundException 找不到流程模型时
     * @author K
     * @since 1.0.0
     */
    fun update(key: String, version: Int? = null, name: String?, svgXml: String?, flowJson: String?): FlowModel

    /**
     * 部署流程，通过已经存在的流程模型。一个版本的模型限制只能部署一次！
     *
     * @param key 流程key(bpmn文件中process元素的id)，不能为空
     * @param version 模型版本，如果为null则使用最新的版本，默认为null
     * @return 流程定义对象
     * @throws IllegalStateException 流程模型重复部署时
     * @throws IllegalArgumentException 流程key为空时
     * @throws ObjectNotFoundException 找不到流程模型时
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
     * @param key 流程key(bpmn文件中process元素的id)。当有多个流程定义时，取第一个！
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

}