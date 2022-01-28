package io.kuark.service.user.common.rbac.vo.group

import io.kuark.base.support.result.IdJsonResult
import java.time.LocalDateTime

class RbacUserGroupDetail: IdJsonResult<String>() {

    /** 用户组编码 */
    var groupCode: String? = null

    /** 用户组名 */
    var groupName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

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