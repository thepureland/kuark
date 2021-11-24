package io.kuark.ability.sys.common.reg.dict

import io.kuark.base.support.payload.ListSearchPayload

/**
 * 字典查询参数
 *
 * @author K
 * @since 1.0.0
 */
class RegDictSearchPayload: ListSearchPayload() {

    /** 主键 */
    var id: String? = null

    /** 模块 */
    var module: String? = null

    /** 字典类型 */
    var dictType: String? = null

    /** 字典名称，或其国际化key */
    var dictName: String? = null

    /** 字典项编号 */
    var itemCode: String? = null

    /** 父项编号 */
    var parentCode: String? = null

    /** 父项主键 */
    var parentId: String? = null

    /** 字典项名称，或其国际化key */
    var itemName: String? = null

    /** 是否启用 */
    var active: Boolean? = null

    /** 是否为第一层树节点 */
    var firstLevel: Boolean? = null

}