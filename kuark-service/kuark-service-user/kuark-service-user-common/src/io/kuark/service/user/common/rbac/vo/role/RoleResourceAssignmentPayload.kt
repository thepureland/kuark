package io.kuark.service.user.common.rbac.vo.role

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

open class RoleResourceAssignmentPayload {

    /** 角色id */
    @get:NotBlank(message = "角色id不能为空！")
    var roleId: String? = null

    /** 角色关联的资源列表 */
    @get:NotNull(message = "角色关联的资源列表不能为NULL！")
    var resourceIds: List<String>? = null

}