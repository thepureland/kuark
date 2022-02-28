package io.kuark.service.user.common.rbac.vo.role


/**
 * 角色-用户关联结果
 *
 * @author K
 * @since 1.0.0
 */
open class RoleUserAssignmentResult {

    /** 用户id */
    var userId: String? = null

    /** 用户名 */
    var username: String? = null

    /** 是否已关联 */
    var assigned: Boolean = false

}