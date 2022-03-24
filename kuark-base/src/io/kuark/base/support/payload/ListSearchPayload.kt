package io.kuark.base.support.payload

import io.kuark.base.query.sort.Order

/**
 * 列表查询条件项载体
 *
 * @author K
 * @since 1.0.0
 */
open class ListSearchPayload: SearchPayload() {

    /** 当前页码(不分页时可将其设置为null) */
    open var pageNo: Int? = 1

    /** 页面大小(仅当pageNo不为null时才应用) */
    open var pageSize: Int? = Int.MAX_VALUE

    /** 排序规则 */
    open var orders: List<Order>? = null

}