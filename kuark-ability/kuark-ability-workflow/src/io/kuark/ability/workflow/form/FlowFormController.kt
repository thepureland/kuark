package io.kuark.ability.workflow.form

import io.kuark.ability.web.common.WebResult
import io.kuark.base.query.sort.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


/**
 * 工作流表单控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("/flow/form")
class FlowFormController {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var flowFormBiz: IFlowFormBiz

    /**
     * 返回指定id的流程表单
     *
     * @param id 表单id
     * @return 统一响应结果，当找不到表单时，属性data为null
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/get"], method = [RequestMethod.GET])
    fun get(id: String): WebResult<FlowForm> {
        val flowForm = flowFormBiz.get(id)
        return if (flowForm == null) {
            WebResult(null,"找不到流程表单！id：$id")
        } else {
            WebResult(flowForm)
        }
    }

    /**
     * 查询表单
     *
     * @param searchParams 查询参数，当查询参数的属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
     * @param pageNum 分页页码，从1开始，默认为1，小于1将按1处理
     * @param pageSize 分页每页最大条数，默认为20，小于1将按不分页处理
     * @param orders 排序规则
     * @return 统一响应结果，当找不到表单时，属性data为空列表
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/search"], method = [RequestMethod.GET])
    fun search(
        searchParams: FlowFormSearchParams, pageNum: Int, pageSize: Int, vararg orders: Order
    ): WebResult<List<FlowForm>> {
        val flowForms = flowFormBiz.search(searchParams, pageNum, pageSize,  *orders)
        return WebResult(flowForms)
    }

    /**
     * 保存表单
     *
     * @param flowForm 流程表单
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/save"], method = [RequestMethod.POST])
    fun save(flowForm: FlowForm): WebResult<Boolean> {
        return WebResult(flowFormBiz.saveOrUpdate(flowForm))
    }

    /**
     * 更新表单
     *
     * @param flowForm 流程表单
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/update"], method = [RequestMethod.PUT])
    fun update(flowForm: FlowForm): WebResult<Boolean> {
        return WebResult(flowFormBiz.saveOrUpdate(flowForm))
    }

    /**
     * 删除表单
     *
     * @param id 表单id
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @RequestMapping(value = ["/delete"], method = [RequestMethod.DELETE])
    fun delete(id: String): WebResult<Boolean> {
        return WebResult(flowFormBiz.deleteById(id))
    }

    //endregion your codes 2

}