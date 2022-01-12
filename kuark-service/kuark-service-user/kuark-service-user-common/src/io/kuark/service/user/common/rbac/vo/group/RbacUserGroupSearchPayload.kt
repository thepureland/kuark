package io.kuark.service.user.common.rbac.vo.group

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

class RbacUserGroupSearchPayload: ListSearchPayload() {

    override var returnEntityClass: KClass<*>? = RbacUserGroupRecord::class

    /** 组编码 */
    var groupCode: String? = null

    /** 组名 */
    var groupName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}