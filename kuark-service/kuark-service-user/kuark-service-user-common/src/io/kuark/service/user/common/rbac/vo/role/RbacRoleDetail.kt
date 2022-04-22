package io.kuark.service.user.common.rbac.vo.role

import io.kuark.base.support.result.IdJsonResult
import java.time.LocalDateTime


class RbacRoleDetail: IdJsonResult<String>() {

    /** 角色编码 */
    var roleCode: String? = null

    /** 角色名 */
    var roleName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 租户名称 */
    var tenantName: String? = null

    /** 记录创建时间 */
    var createTime: LocalDateTime? = null

    /** 记录创建用户 */
    var createUser: String? = null

    /** 记录更新时间 */
    var updateTime: LocalDateTime? = null

    /** 记录更新用户 */
    var updateUser: String? = null

    /** 是否内置 */
    var builtIn: Boolean? = null

    /** 备注 */
    var remark: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}