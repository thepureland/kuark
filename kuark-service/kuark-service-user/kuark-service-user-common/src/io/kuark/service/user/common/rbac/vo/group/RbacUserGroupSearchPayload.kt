package io.kuark.service.user.common.rbac.vo.group

import io.kuark.base.query.enums.Operator
import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

class RbacUserGroupSearchPayload: ListSearchPayload() {

    override var returnEntityClass: KClass<*>? = RbacUserGroupRecord::class

    override fun getOperators(): Map<String, Operator> {
        return mapOf(
            this::groupCode.name to Operator.ILIKE,
            this::groupName.name to Operator.ILIKE,
        )
    }

    /** 组编码 */
    var groupCode: String? = null

    /** 组名 */
    var groupName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}