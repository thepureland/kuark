package io.kuark.service.sys.common.vo.dict

import java.io.Serializable

class SysDictRecord: Serializable {

    /** 模块 */
    var module: String? = null

    /** 字典id */
    var dictId: String? = null

    /** 字典类型 */
    var dictType: String? = null

    /** 字典名称，或其国际化key */
    var dictName: String? = null

    /** 字典项ID */
    var itemId: String? = null

    /** 字典项编号 */
    var itemCode: String? = null

    /** 父项ID */
    var parentId: String? = null

    /** 字典项名称，或其国际化key */
    var itemName: String? = null

    /** 该字典编号在同父节点下的排序号 */
    var seqNo: Int? = null

    /** 是否启用 */
    var active: Boolean? = null

    /** 备注 */
    var remark: String? = null

    /** 父项编号 */
    var parentCode: String? = null

    /** 所有父项ID */
    var parentIds: List<String>? = null

}