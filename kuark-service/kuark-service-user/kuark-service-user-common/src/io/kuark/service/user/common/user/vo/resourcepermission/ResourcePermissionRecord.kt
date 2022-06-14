package io.kuark.service.user.common.user.vo.resourcepermission

import io.kuark.base.support.result.IdJsonResult


open class ResourcePermissionRecord : IdJsonResult<String>() {

    /** 名称 */
    var name: String? = null

    /** URL */
    var url: String? = null

    /** 关联的角色 */
    var roleNames: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 父id */
    var parentId: String? = null

}