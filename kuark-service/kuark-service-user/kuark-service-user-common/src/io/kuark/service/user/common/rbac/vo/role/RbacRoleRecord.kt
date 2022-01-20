package io.kuark.service.user.common.rbac.vo.role

import io.kuark.base.support.result.IdJsonResult
import java.time.LocalDateTime

class RbacRoleRecord: IdJsonResult<String>() {

    /** 角色编码 */
    var roleCode: String? = null

    /** 角色名 */
    var roleName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String? = null

    /** 记录创建时间 */
    var createTime: LocalDateTime? = null

    /** 是否启用 */
    var active: Boolean? = null

    /** 备注 */
    var remark: String? = null

}