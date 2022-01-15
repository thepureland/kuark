package io.kuark.service.user.common.rbac.vo.group

import java.io.Serializable
import java.time.LocalDateTime

class RbacUserGroupDetail: Serializable {

    /** 主键 */
    var id: String? = null

    /** 用户组编码 */
    var groupCode: String? = null

    /** 用户组名 */
    var groupName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String? = null

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