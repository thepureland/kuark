package io.kuark.service.sys.common.vo.domain

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass


/**
 * 域名查询条件载体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysDomainSearchPayload : ListSearchPayload() {
//endregion your codes 1

    //region your codes 2

    override var returnEntityClass: KClass<*>? = SysDomainRecord::class

    /** 域名 */
    var domain: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    //endregion your codes 2

}