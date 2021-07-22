package io.kuark.ability.workflow.ibiz

import io.kuark.ability.workflow.vo.FlowDefinition
import io.kuark.base.io.PathKit
import org.activiti.engine.ActivitiObjectNotFoundException
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.IllegalArgumentException

/**
 * 流程定义相关业务接口
 *
 * @author K
 * @since 1.0.0
 */
interface IFlowDefinitionBiz {

    /**
     * 部署单个流程：通过指定的bpmn相关信息
     *
     * @param deploymentName 部署名称
     * @param bpmnFileName bpmn文件名，不传后缀(.bpmn)时将自动拼接
     * @param diagramFileName bpmn对应的流程图文件名，不传后缀(.png)时将自动拼接。可选，不指定时将不会部署流程图。
     * @param prefixPath 目录前缀，默认为bpmn。bpmn和流程图文件都必须放置于该目录下
     * @return 流程定义对象。
     * @throws FileNotFoundException 文件找不到时
     * @author K
     * @since 1.0.0
     */
    fun deployWithBpmn(
        deploymentName: String, bpmnFileName: String, diagramFileName: String? = null, prefixPath: String = "bpmn"
    ): FlowDefinition

    /**
     * 部署多个流程：通过zip压缩包。包中可包含bpmn和流程图文件,文件名(不含后缀)一样视为同一流程，流程图为可选。
     *
     * @param deploymentName 部署名称
     * @param zipFileName 压缩包文件名，不传后缀(.zip)时将自动拼接。
     * @param prefixPath 目录前缀，默认为bpmn。zip包必须放置于该目录下
     * @return List(流程定义对象)
     * @throws FileNotFoundException 文件找不到时
     * @author K
     * @since 1.0.0
     */
    fun deployWithZip(
        deploymentName: String,
        zipFileName: String,
        prefixPath: String = PathKit.getResourcePath("bpmn")
    ): List<FlowDefinition>

    /**
     * 返回指定key的流程定义
     *
     * 尽管同一个key关联多个流程定义在activiti中是允许的，但在实际应用中最好是一一对应的关系！
     *
     * @param definitionKey 流程定义key
     * @return List(流程定义对象)，找不到返回空列表
     * @author K
     * @since 1.0.0
     */
    fun getDefinitionByKey(definitionKey: String): List<FlowDefinition>

    /**
     * 返回指定部署id对应的流程定义
     *
     * @param deploymentId 部署id
     * @return List(流程定义对象)，找不到返回空列表
     * @author K
     * @since 1.0.0
     */
    fun getDefinitionsByDeploymentId(deploymentId: String): List<FlowDefinition>

    /**
     * 激活流程定义，重复激活将忽略操作
     *
     * @param definitionKey 流程定义key
     * @throws IllegalArgumentException 当指定的definitionKey找不到对应流程定义时
     * @author K
     * @since 1.0.0
     */
    fun activateDefinition(definitionKey: String)

    /**
     * 挂起流程定义，重复挂起将忽略操作
     *
     * @param definitionKey 流程定义key
     * @throws IllegalArgumentException 当指定的definitionKey找不到对应流程定义时
     * @author K
     * @since 1.0.0
     */
    fun suspendDefinition(definitionKey: String)

    /**
     * 删除指定部署id对应的流程相关信息
     *
     * @param deploymentId 部署id
     * @param cascade 是否级联删除流程实例、历史流程实例和job, 默认为否
     * @throws IllegalArgumentException 当指定的部署id找不到对应信息时
     * @author K
     * @since 1.0.0
     */
    fun deleteDefinitions(deploymentId: String, cascade: Boolean = false)

    /**
     * 获取流程图
     *
     * @param definitionKey 流程定义key。当有多个流程定义时，取第一个！
     * @return 图片输入流对象，流程定义不存在时返回null
     * @author K
     * @since 1.0.0
     */
    fun getFlowDiagram(definitionKey: String): InputStream?

    /**
     * 生成流程图(可以在部署前)
     *
     * @param bpmnFileName bpmn文件名，不传后缀(.bpmn)时将自动拼接
     * @param prefixPath 目录前缀，默认为bpmn。bpmn文件必须放置于该目录下
     * @return 图片输入流对象
     * @throws FileNotFoundException 文件找不到时
     * @author K
     * @since 1.0.0
     */
    fun genFlowDiagram(bpmnFileName: String, prefixPath: String = PathKit.getResourcePath("bpmn")): InputStream

}