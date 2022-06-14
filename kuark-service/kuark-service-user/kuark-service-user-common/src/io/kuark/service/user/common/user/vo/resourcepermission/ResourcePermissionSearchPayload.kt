package io.kuark.service.user.common.user.vo.resourcepermission

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.base.support.payload.ListSearchPayload
import javax.validation.constraints.NotBlank

open class ResourcePermissionSearchPayload : ListSearchPayload() {

    /** 子系统代码 */
    @get:NotBlank(message = "子系统不能为空！")
    @get:DictCode(message = "子系统不在取值范围内！", module = "kuark:sys", dictType = "sub_sys")
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 资源类型代码 */
    @get:NotBlank(message = "资源类型不能为空！")
    @get:DictCode(message = "资源类型不在取值范围内！", module = "kuark:sys", dictType = "resource_type")
    var resourceTypeDictCode: String? = null

    /** 资源名称 */
    var name: String? = null

}