package io.kuark.service.sys.common.model.resource

import io.kuark.base.bean.validation.constraint.annotaions.DictEnumCode
import org.hibernate.validator.constraints.URL
import javax.validation.constraints.Digits
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

class SysResourcePayload {

    @get:NotBlank(message = "资源名称不能为空！")
    /** 名称，或其国际化key */
    var name: String? = null

    @get:URL
    /** url */
    var url: String? = null

    @get:DictEnumCode(enumClass = ResourceTypeEnum::class, message = "资源类型不存在！")
    /** 资源类型字典代码 */
    var resourceTypeDictCode: String?  = null

    /** 父id */
    var parentId: String? = null

    @get:Positive(message = "序号必须为正数！")
    @get:Digits(integer = 9, fraction = 0, message = "序号必须为整数！")
    @get:Max(value = 999999999, message = "序号不能大于999999999！")
    /** 在同父节点下的排序号 */
    var seqNo: Int? = null


    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 图标 */
    var icon: String? = null

}