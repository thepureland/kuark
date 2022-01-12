package io.kuark.service.user.common.rbac.vo.role

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

class RbacRoleSearchPayload: ListSearchPayload() {

    override var returnEntityClass: KClass<*>? = RbacRoleRecord::class

    /** 角色编码 */
    var roleCode: String? = null

    /** 角色名 */
    var roleName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}