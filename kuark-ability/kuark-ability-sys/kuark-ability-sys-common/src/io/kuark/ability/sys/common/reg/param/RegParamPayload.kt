package io.kuark.ability.sys.common.reg.param

import javax.validation.constraints.Digits
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive


class RegParamPayload {

    /** 主键 */
    var id: String? = null

    @get:NotBlank(message = "模块不能为空！")
    /** 模块 */
    var module: String? = null

    /** 参数名称 */
    @get:NotBlank(message = "参数名称不能为空！")
    var paramName: String? = null

    /** 参数值，或其国际化key */
    var paramValue: String? = null

    /** 默认参数值，或其国际化key */
    var defaultValue: String? = null

    @get:Positive(message = "序号必须为正数！")
    @get:Digits(integer = 9, fraction = 0, message = "序号必须为整数！")
    @get:Max(value = 999999999, message = "序号不能大于999999999！")
    /** 序号 */
    var seqNo: Int? = null

    /** 备注 */
    var remark: String? = null

}