package io.kuark.service.auth.common.rbac.vo.role

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

class AuthRoleSearchPayload: ListSearchPayload() {

    override var returnEntityClass: KClass<*>? = AuthRoleRecord::class

    /** 角色编码 */
    var roleCode: String? = null

    /** 角色名 */
    var roleName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}