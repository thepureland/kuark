package io.kuark.ability.sys.common.vo.reg.dict

data class RegDictRecord(

    /** 模块 */
    val module: String?,

    /** 字典id */
    val dictId: String,

    /** 字典类型 */
    val dictType: String,

    /** 字典名称，或其国际化key */
    val dictName: String?,

    /** 字典项ID */
    val itemId: String?,

    /** 字典项编号 */
    val itemCode: String?,

    /** 父项ID */
    val parentId: String?,

    /** 字典项名称，或其国际化key */
    val itemName: String?,

    /** 该字典编号在同父节点下的排序号 */
    val seqNo: Int?,

    /** 是否启用 */
    val active: Boolean?,

    /** 备注 */
    val remark: String?

) {

    /** 父项编号 */
    var parentCode: String? = null

    /** 所有父项ID */
    var parentIds: List<String>? = null

}