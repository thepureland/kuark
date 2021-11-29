package io.kuark.service.sys.common.vo.reg.dict

import io.kuark.base.bean.validation.constraint.annotaions.AtLeast
import io.kuark.base.bean.validation.support.AssertLogic
import javax.validation.constraints.Digits
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@AtLeast(
    properties = ["parentId", "module"],
    logic = AssertLogic.IS_NOT_BLANK,
    message = "parentId、module两个至少一个不能为空"
)
class RegDictPayload {

    /** 主键 */
    var id: String? = null

    /** 字典id */
    var dictId: String? = null

    /** 字典类型 */
    var dictType: String? = null

    /** 父项id */
    var parentId: String? = null

    /** 模块 */
    var module: String? = null

    /** 编码 */
    @get:NotBlank(message = "编码不能为空！")
    var code: String? = null

    /** 名称 */
    @get:NotBlank(message = "名称不能为空！")
    var name: String? = null

    /** 序号 */
    @get:Positive(message = "序号必须为正数！")
    @get:Digits(integer = 9, fraction = 0, message = "序号必须为整数！")
    @get:Max(value = 999999999, message = "序号不能大于999999999！")
    var seqNo: Int? = null

    /** 备注 */
    var remark: String? = null

}