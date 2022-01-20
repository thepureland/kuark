package io.kuark.service.user.common.rbac.vo.role

import io.kuark.base.support.payload.FormPayload
import javax.validation.constraints.NotBlank

class RbacRolePayload: FormPayload<String>() {

    /** 角色编码 */
    @get:NotBlank(message = "角色编码不能为空！")
    var roleCode: String? = null

    /** 角色名 */
    @get:NotBlank(message = "角色名不能为空！")
    var roleName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String? = null

    /** 备注 */
    var remark: String? = null

}