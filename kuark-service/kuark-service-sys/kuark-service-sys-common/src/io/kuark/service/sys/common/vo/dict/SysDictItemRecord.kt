package io.kuark.service.sys.common.vo.dict

import io.kuark.base.support.result.IJsonResult

class SysDictItemRecord(
    /** 主键 */
    val id: String,

    /** 字典项编号 */
    val itemCode: String,

    /** 字典项名称，或其国际化key */
    val itemName: String,

    /** 父项主键 */
    val parentId: String?,

    /** 该字典编号在同父节点下的排序号 */
    val seqNo: Int? = null
): IJsonResult