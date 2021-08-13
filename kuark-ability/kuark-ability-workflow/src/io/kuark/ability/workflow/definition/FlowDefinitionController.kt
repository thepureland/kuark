package io.kuark.ability.workflow.definition

import io.kuark.ability.web.common.WebResult
import io.kuark.base.image.ImageKit
import io.kuark.base.query.sort.Order
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

    private lateinit var flowDefinitionBiz: IFlowDefinitionBiz

    @RequestMapping(value = ["/get"], method = [RequestMethod.GET])
    fun get(key: String, version: Int): WebResult<FlowDefinition> {
        val definition = flowDefinitionBiz.get(key, version)
        return if (definition == null) {
            WebResult("流程定义不存在！key：$key, version：$version")
        } else {
            WebResult(definition)
        }
    }

    @RequestMapping(value = ["/search"], method = [RequestMethod.GET])
    fun search(
        queryItems: FlowDefinitionQueryItems,
        orders: List<Order>?,
        pageNum: Int,
        pageSize: Int
    ): WebResult<List<FlowDefinition>> {
        val definitions = flowDefinitionBiz.search(queryItems, orders, pageNum, pageSize)
        return WebResult(definitions)
    }

    @RequestMapping(value = ["/create"], method = [RequestMethod.POST])
    fun create(def: FlowDefinition, flowJson: String, svgXml: String): WebResult<Boolean> {
        flowDefinitionBiz.create(def.key, def.name, def.category, flowJson, svgXml, def.tenantId)
        return WebResult(true)
    }

    @RequestMapping(value = ["/update"], method = [RequestMethod.PUT])
    fun update(def: FlowDefinition, flowJson: String?, svgXml: String?): WebResult<Boolean> {
        flowDefinitionBiz.update(def.key, def.version, def.name, def.category, svgXml, flowJson, def.tenantId)
        return WebResult(true)
    }

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

    fun delete() {

    }

    fun deleteThorough() {

    }


}