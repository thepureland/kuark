package io.kuark.service.user.common.rbac.vo.role

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


/**
 * 角色-用户关联载休
 *
 * @author K
 * @since 1.0.0
 */
open class RoleUserAssignmentPayload {

    /** 角色id */
    @get:NotBlank(message = "角色id不能为空！")
    var roleId: String? = null

    /** 关联的用户id列表 */
    @get:NotNull(message = "关联的用户id列表不能为NULL！")
    var userIds: List<String>? = null

}