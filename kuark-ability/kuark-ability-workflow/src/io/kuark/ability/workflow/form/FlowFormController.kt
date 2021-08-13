package io.kuark.ability.workflow.form

import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
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

    @RequestMapping(value = ["/get"], method = [RequestMethod.GET])
    fun get(id: String): FlowForm? {
        return flowFormBiz.getById(id)
    }

    @RequestMapping(value = ["/search"], method = [RequestMethod.GET])
    fun search(queryItems: FlowFormQueryItems, orders: List<Order>?, pageNum: Int, pageSize: Int) {
        flowFormBiz.search(queryItems, orders, pageNum, pageSize)
    }

    @RequestMapping(value = ["/save"], method = [RequestMethod.PUT])
    fun save(flowForm: FlowForm) {
        flowFormBiz.saveOrUpdate(flowForm)
    }

    @RequestMapping(value = ["/delete"], method = [RequestMethod.DELETE])
    fun delete(id: String) {
        flowFormBiz.deleteById(id)
    }

    //endregion your codes 2

}