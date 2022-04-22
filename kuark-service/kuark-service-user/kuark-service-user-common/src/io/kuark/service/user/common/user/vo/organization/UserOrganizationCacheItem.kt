package io.kuark.service.user.common.user.vo.organization

import java.io.Serializable

class UserOrganizationCacheItem: Serializable {

    companion object {
        private const val serialVersionUID = 4090323185007037711L
    }

    /** 主键 */
    var id: String? = null

    /** 名称 */
    var name: String? = null

    /** 简称 */
    var abbrName: String? = null

    /** 组织类型 */
    var orgTypeDictCode: String? = null

    /** 父id */
    var parentId: String? = null

    /** 在同父节点下的排序号 */
    var seqNo: Int? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 备注 */
    var remark: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}