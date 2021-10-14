package io.kuark.service.sys.common.model.dict

import io.kuark.base.query.sort.Order
import io.kuark.base.support.payload.SearchPayload

/**
 * 字典查询参数
 *
 * @author K
 * @since 1.0.0
 */
class SysDictSearchPayload: SearchPayload() {

    /** 模块 */
    var module: String? = null

    /** 字典类型 */
    var dictType: String? = null

    /** 字典名称，或其国际化key */
    var dictName: String? = null

    /** 字典项编号 */
    var itemCode: String? = null

    /** 字典项名称，或其国际化key */
    var itemName: String? = null

    /** 是否启用 */
    var isActive: Boolean? = null

    override var pageNo: Int? = 1

    override var pageSize: Int? = 10

}