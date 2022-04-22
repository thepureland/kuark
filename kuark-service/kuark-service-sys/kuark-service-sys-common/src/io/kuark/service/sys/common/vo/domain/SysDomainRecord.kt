package io.kuark.service.sys.common.vo.domain

import io.kuark.base.support.result.IdJsonResult


/**
 * 域名查询记录
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysDomainRecord : IdJsonResult<String>() {
//endregion your codes 1

    //region your codes 2

    /** 域名 */
    var domain: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    @Transient
    var tenantId: String? = null

    /** 租户名称 */
    var tenantName: String? = null

    /** 备注，或其国际化key */
    var remark: String? = null

    /** 是否启用 */
    var active: Boolean? = null

    //endregion your codes 2

}