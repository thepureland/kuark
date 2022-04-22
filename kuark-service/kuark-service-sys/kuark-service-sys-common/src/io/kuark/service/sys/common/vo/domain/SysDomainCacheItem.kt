package io.kuark.service.sys.common.vo.domain

import java.io.Serializable

class SysDomainCacheItem: Serializable {

    companion object {
        private const val serialVersionUID = 3090923885057037718L
    }

    /** 域名 */
    var domain: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 备注，或其国际化key */
    var remark: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}