package io.kuark.service.workflow.provider.ibiz

import io.kuark.base.support.biz.IBaseBiz
import io.kuark.service.workflow.common.vo.form.FlowFormSearchParams
import io.kuark.service.workflow.provider.model.po.FlowForm
import io.kuark.base.query.sort.Order

/**
 * 工作流表单业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IFlowFormBiz : IBaseBiz<String, FlowForm> {
//endregion your codes 1

    //region your codes 2

    /**
     * 返回指定key和版本的表单
     *
     * @param key 表单key
     * @param version 表单版本，如果为null，返回最新版本，默认为null
     * @return 流程表单对象，找不到返回null
     * @throws IllegalArgumentException key为空时
     * @author K
     * @since 1.0.0
     */
    fun get(key: String, version: Int? = null): FlowForm?

    /**
     * 新增或更新表单信息
     *
     * @param flowForm 表单对象
     * @return 是否保存或更新成功， true：成功，false：失败
     * @author K
     * @since 1.0.0
     */
    fun saveOrUpdate(flowForm: FlowForm): Boolean

    /**
     * 查询表单
     *
     * @param searchParams 查询参数，当查询参数的属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
     * @param pageNum 分页页码，从1开始，默认为1，小于1将按1处理
     * @param pageSize 分页每页最大条数，默认为20，小于1将按不分页处理
     * @param orders 排序规则
     * @return List(流程表单对象)
     * @author K
     * @since 1.0.0
     */
    fun search(
        searchParams: FlowFormSearchParams,
        pageNum: Int = 1,
        pageSize: Int = 20,
        vararg orders: Order
    ): List<FlowForm>

    //endregion your codes 2

}