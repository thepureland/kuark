package io.kuark.ability.workflow.form

import io.kuark.ability.data.rdb.biz.IBaseBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.query.sort.Order

/**
 * 工作流表单业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IFlowFormBiz: IBaseBiz<String, FlowForm> {
//endregion your codes 1

    //region your codes 2

    /**
     * 新增或更新表单信息
     *
     * @param flowForm 表单对象
     * @author K
     * @since 1.0.0
     */
    fun saveOrUpdate(flowForm: FlowForm)

    /**
     * 查询表单
     *
     * @param queryItems 查询项，当查询项的属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
     * @param orders 排序规则
     * @param pageNum 分页页码，从1开始，默认为1，小于1将按1处理
     * @param limit 分页每页最大条数，默认为20，小于1将按不分页处理
     * @return List(流程表单对象)
     * @author K
     * @since 1.0.0
     */
    fun search(queryItems: FlowFormQueryItems, orders: List<Order>?, pageNum: Int = 1, limit: Int = 20): List<FlowForm>

    //endregion your codes 2

}