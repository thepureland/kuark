package io.kuark.service.user.common.rbac.vo.group

import io.kuark.base.support.payload.FormPayload
import javax.validation.constraints.NotBlank

class RbacUserGroupPayload: FormPayload<String>() {

    /** 组编码 */
    @get:NotBlank(message = "组编码不能为空！")
    var groupCode: String? = null

    /** 组名 */
    @get:NotBlank(message = "组名不能为空！")
    var groupName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 备注 */
    var remark: String? = null

}