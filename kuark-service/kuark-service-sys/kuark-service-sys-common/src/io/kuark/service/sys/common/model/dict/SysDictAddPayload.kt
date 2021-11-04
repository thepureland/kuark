package io.kuark.service.sys.common.model.dict

import io.kuark.base.bean.validation.constraint.annotaions.AtLeast
import io.kuark.base.bean.validation.support.AssertLogic
import javax.validation.constraints.*

@AtLeast(
    properties = ["parentId", "module"],
    logic = AssertLogic.IS_NOT_BLANK,
    message = "parentId、module两个至少一个不能为空"
)
class SysDictAddPayload {

    /** 字典id */
    var dictId: String? = null

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