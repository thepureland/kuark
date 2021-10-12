package io.kuark.base.support.payload

import io.kuark.base.query.sort.Order

/**
 * 查询条件载体父类
 *
 * 查询规则:
 *    1. 各个属性只有当其值非空时才会应用该查询条件
 *    2. 各个属性的查询逻辑由用户决定
 *    3. 各属性间的关系默认为AND, 可通过重写and属性改变
 *
 * @author K
 * @since 1.0.0
 */
open class SearchPayload {

    /** 当前页码 */
    var pageNo: Int? = null

    /** 页面大小 */
    var pageSize: Int? = null

    /** 排序规则 */
    var orders: List<Order>? = null

    /** 各属性间的查询逻辑关系 */
    var and: Boolean = true

}