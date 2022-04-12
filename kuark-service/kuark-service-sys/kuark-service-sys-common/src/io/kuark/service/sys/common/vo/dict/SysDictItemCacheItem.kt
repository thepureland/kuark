package io.kuark.service.sys.common.vo.dict

import java.io.Serializable


/**
 * 字典项的缓存项
 *
 * @author K
 * @since 1.0.0
 */
class SysDictItemCacheItem: Serializable {

    companion object {
        private const val serialVersionUID = 5990933382657637736L
    }


    /** 字典项编号 */
    var itemCode: String? = null

    /** 字典项名称，或其国际化key */
    var itemName: String? = null

    /** 父项ID */
    var parentId: String? = null

    /** 该字典编号在同父节点下的排序号 */
    var seqNo: Int? = null

}