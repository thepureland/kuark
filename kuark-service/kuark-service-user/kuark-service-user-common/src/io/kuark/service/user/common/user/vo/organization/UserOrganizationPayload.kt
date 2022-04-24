package io.kuark.service.user.common.user.vo.organization

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.base.support.payload.FormPayload
import javax.validation.constraints.NotBlank

open class UserOrganizationPayload: FormPayload<String>() {

    /** 名称 */
    @get:NotBlank(message = "名称不能为空！")
    var name: String? = null

    /** 简称 */
    var abbrName: String? = null

    /** 组织类型 */
    @get:NotBlank(message = "组织类型不能为空！")
    var orgTypeDictCode: String? = null

    /** 父id */
    var parentId: String? = null

    /** 在同父节点下的排序号 */
    var seqNo: Int? = null

    /** 子系统代码 */
    @get:NotBlank(message = "子系统不能为空！")
    @get:DictCode(message = "子系统不在取值范围内！", module = "kuark:sys", dictType = "sub_sys")
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 备注 */
    var remark: String? = null

    /** 所有父id列表 */
    var parentIds: List<String>? = null

}