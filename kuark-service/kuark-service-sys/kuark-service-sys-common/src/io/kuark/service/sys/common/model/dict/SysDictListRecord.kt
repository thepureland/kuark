package io.kuark.service.sys.common.model.dict

data class SysDictListRecord(

    /** 模块 */
    val module: String?,

    /** 字典类型 */
    val dictType: String,

    /** 字典名称，或其国际化key */
    val dictName: String?,

    /** 字典项ID */
    val itemId: String?,

    /** 字典项编号 */
    val itemCode: String?,

    /** 父项编号 */
    val parentCode: String?,

    /** 字典项名称，或其国际化key */
    val itemName: String?,

    /** 该字典编号在同父节点下的排序号 */
    val seqNo: Int?,

    /** 是否启用 */
    val active: Boolean?

)