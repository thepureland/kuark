package io.kuark.service.user.common.user.vo.resourcepermission


/**
 * 资源-角色关联结果
 *
 * @author K
 * @since 1.0.0
 */
open class ResourceRoleAssignmentResult {

    /** 角色id */
    var roleId: String? = null

    /** 角色名 */
    var roleName: String? = null

    /** 是否已关联 */
    var assigned: Boolean = false

}