package io.kuark.service.user.common.rbac.vo.role

import java.io.Serializable


/**
 * 角色的缓存项
 *
 * @author K
 * @since 1.0.0
 */
class RbacRoleCacheItem: Serializable {

    companion object {
        private const val serialVersionUID = 7891923881657037158L
    }


    /** 主键 */
    var id: String? = null

    /** 角色编码 */
    var roleCode: String? = null

    /** 角色名 */
    var roleName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 是否内置 */
    var builtIn: Boolean? = null

    /** 备注 */
    var remark: String? = null

    /** 是否启用 */
    @Transient
    var active: Boolean? = null

}