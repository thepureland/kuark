package io.kuark.service.user.common.user.vo.resourcepermission

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


/**
 * 资源-角色关联载休
 *
 * @author K
 * @since 1.0.0
 */
open class ResourceRoleAssignmentPayload {

    /** 资源id */
    @get:NotBlank(message = "资源id不能为空！")
    var resourceId: String? = null

    /** 关联的角色id列表 */
    @get:NotNull(message = "关联的角色id列表不能为NULL！")
    var roleIds: List<String>? = null

}