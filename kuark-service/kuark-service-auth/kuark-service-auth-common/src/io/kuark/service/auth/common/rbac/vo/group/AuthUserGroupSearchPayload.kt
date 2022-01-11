package io.kuark.service.auth.common.rbac.vo.group

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

class AuthUserGroupSearchPayload: ListSearchPayload() {

    override var returnEntityClass: KClass<*>? = AuthUserGroupRecord::class

    /** 组编码 */
    var groupCode: String? = null

    /** 组名 */
    var groupName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}