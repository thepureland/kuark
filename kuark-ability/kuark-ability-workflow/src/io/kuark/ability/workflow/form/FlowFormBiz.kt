package io.kuark.ability.workflow.form

import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.query.sort.Order
import org.springframework.stereotype.Service

/**
 * 工作流表单业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class FlowFormBiz : BaseBiz<String, FlowForm, FlowFormDao>(), IFlowFormBiz {
//endregion your codes 1

    //region your codes 2

    override fun saveOrUpdate(flowForm: FlowForm) {
        if (StringKit.isBlank(flowForm.id)) {
            dao.insert(flowForm)
        } else {
            dao.update(flowForm)
        }
    }

    override fun search(
        queryItems: FlowFormQueryItems, orders: List<Order>?, pageNum: Int, limit: Int
    ): List<FlowForm> {
        val criteria = Criteria()
        if (StringKit.isNotBlank(queryItems.key)) {
            criteria.addAnd(FlowForm::key.name, Operator.ILIKE, queryItems.key)
        }
        if (StringKit.isNotBlank(queryItems.name)) {
            criteria.addAnd(FlowForm::name.name, Operator.ILIKE, queryItems.name)
        }
        if (StringKit.isNotBlank(queryItems.categoryDictCode)) {
            criteria.addAnd(FlowForm::categoryDictCode.name, Operator.EQ, queryItems.categoryDictCode)
        }
        if (queryItems.version == null) {
            if (queryItems.latestOnly) {
                val latestVersion = dao.max(FlowForm::version.name, criteria)
                latestVersion ?: return emptyList()
            }
        } else {
            criteria.addAnd(FlowForm::version.name, Operator.EQ, queryItems.version)
        }
        val orderArray = orders?.toTypedArray() ?: emptyArray()
        val pageNo = if (pageNum < 1) 1 else pageNum
        return dao.pagingSearch(criteria, pageNo, limit, *orderArray)
    }

    //endregion your codes 2

}