package io.kuark.ability.workflow.definition

import io.kuark.ability.web.common.WebResult
import io.kuark.base.image.ImageKit
import io.kuark.base.query.sort.Order
import io.kuark.ability.sys.provider.reg.ibiz.IRegDictItemBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.imageio.ImageIO

/**
 * 流程定义控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
@RequestMapping("/flow/definition")
class FlowDefinitionController {

    @Autowired
    private lateinit var flowDefinitionBiz: IFlowDefinitionBiz

    @Autowired
    private lateinit var regDictItemBiz: IRegDictItemBiz

    /**
     * 返回指定key和版本的流程定义
     *
     * @param key 流程定义key(bpmn文件中process元素的id)
     * @param version 流程定义版本
     * @return 统一响应结果，当找不到表单时，属性data为null
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/get"], method = [RequestMethod.GET])
    fun get(key: String, version: Int): WebResult<FlowDefinition> {
        val definition = flowDefinitionBiz.get(key, version)
        return if (definition == null) {
            WebResult(null,"流程定义不存在！key：$key, version：$version")
        } else {
            WebResult(definition)
        }
    }

    /**
     * 查询流程定义
     *
     * @param searchItems 查询参数，当查询参数的属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
     * @param pageNum 分页页码，从1开始，默认为1，小于1将按1处理
     * @param pageSize 分页每页最大条数，默认为20，小于1将按不分页处理
     * @param orders 排序规则
     * @return 统一响应结果，当找不到时，属性data为空列表
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/search"], method = [RequestMethod.GET])
    fun search(
        searchItems: FlowDefinitionSearchParams,
        pageNum: Int,
        pageSize: Int,
        vararg orders: Order
    ): WebResult<List<FlowDefinition>> {
        val definitions = flowDefinitionBiz.search(searchItems, pageNum, pageSize, *orders)
        return WebResult(definitions)
    }

    /**
     * 创建流程定义
     *
     * @param def 流程定义对象
     * @param flowJson 流程的json内容，不能为空
     * @param svgXml 流程图(svg格式)的xml内容，不能为空
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/create"], method = [RequestMethod.POST])
    fun create(def: FlowDefinition, flowJson: String, svgXml: String): WebResult<Boolean> {
        flowDefinitionBiz.create(def.key, def.name, def.category, flowJson, svgXml, def.tenantId)
        return WebResult(true)
    }

    /**
     * 更新流程定义
     *
     * @param def 流程定义对象
     * @param flowJson 流程的json内容，不能为空
     * @param svgXml 流程图(svg格式)的xml内容，不能为空
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/update"], method = [RequestMethod.PUT])
    fun update(def: FlowDefinition, flowJson: String?, svgXml: String?): WebResult<Boolean> {
        flowDefinitionBiz.update(def.key, def.version, def.name, def.category, flowJson, svgXml, def.tenantId)
        return WebResult(true)
    }

    /**
     * 部署流程
     *
     * @param key 流程定义key(bpmn文件中process元素的id)
     * @param version 流程定义版本
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/deploy"], method = [RequestMethod.POST])
    fun deploy(key: String, version: Int): WebResult<Boolean> {
        flowDefinitionBiz.deploy(key, version)
        return WebResult(true)
    }

    /**
     * 返回流程图
     *
     * @param key 流程定义key(bpmn文件中process元素的id)
     * @param version 流程定义版本
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/getFlowDiagram"], method = [RequestMethod.GET])
    fun getFlowDiagram(key: String, version: Int): WebResult<String> {
        val inputStream = flowDefinitionBiz.getFlowDiagram(key, version)
        return if (inputStream == null) {
            WebResult("获取流程图失败！key：$key, version：$version")
        } else {
            val bufferedImage = ImageIO.read(inputStream)
            val imageStr = ImageKit.imageToString(bufferedImage, "png")
            WebResult(imageStr)
        }
    }

    /**
     * 删除流程定义
     *
     * @param key 流程定义key(bpmn文件中process元素的id)
     * @param version 流程定义版本
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/delete"], method = [RequestMethod.DELETE])
    fun delete(key: String, version: Int): WebResult<Boolean> {
        flowDefinitionBiz.delete(key, version)
        return WebResult(true)
    }

    /**
     * 强制删除流程定义，将级联删除流程实例、历史流程实例和job
     *
     * @param key 流程定义key(bpmn文件中process元素的id)
     * @param version 流程定义版本
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/deleteThorough"], method = [RequestMethod.DELETE])
    fun deleteThorough(key: String, version: Int): WebResult<Boolean> {
        flowDefinitionBiz.delete(key, version, true)
        return WebResult(true)
    }

    @GetMapping("/loadCategories")
    fun loadCategories(): WebResult<Map<String, String>> {
        val items = regDictItemBiz.getItemsByModuleAndType("kuark:flow", "flow_category")
        val map = mutableMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return WebResult(map)
    }

}