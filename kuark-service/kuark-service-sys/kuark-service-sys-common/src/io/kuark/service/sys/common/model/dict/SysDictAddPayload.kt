package io.kuark.service.sys.common.model.dict

import io.kuark.base.bean.validation.constraint.annotaions.AtLeast
import io.kuark.base.bean.validation.support.AssertLogic
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@AtLeast(
    properties = ["parentId", "module"],
    logic = AssertLogic.IS_NOT_BLANK,
    message = "parentId、module两个至少一个不能为空"
)
class SysDictAddPayload {

    /** 父项id */
    var parentId: String? = null

    /** 模块 */
    var module: String? = null

    /** 编码 */
    @get:NotBlank
    var code: String? = null

    /** 名称 */
    @get:NotBlank
    var name: String? = null

    /** 序号 */
    @get:Min(1)
    @get:Max(Int.MAX_VALUE.toLong())
    var seqNo: Int? = null

    /** 备注 */
    var remark: String? = null

}