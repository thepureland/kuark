package io.kuark.service.user.common.user.vo.account

import io.kuark.base.query.enums.Operator
import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

open class UserAccountSearchPayload: ListSearchPayload() {

    override var returnEntityClass: KClass<*>? = UserAccountRecord::class

    override var operators: Map<String, Operator>? = mapOf(this::username.name to Operator.ILIKE)

    /** 用户名 */
    var username: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

}