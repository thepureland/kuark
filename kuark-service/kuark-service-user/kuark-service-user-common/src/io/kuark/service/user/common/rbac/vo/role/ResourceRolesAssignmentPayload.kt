package io.kuark.service.user.common.rbac.vo.role

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import javax.validation.constraints.NotBlank

class ResourceRolesAssignmentPayload {

    @get:NotBlank(message = "资源id不能为空！")
    var resourceId: String? = null

    @get:NotBlank(message = "子系统不能为空！")
    @get:DictCode(message = "子系统不在取值范围内！", module = "kuark:sys", dictType = "sub_sys")
    var subSysDictCode: String? = null

    var tenantId: String? = null

    var roleIds: Set<String>? = null

}