package io.kuark.service.sys.common.vo.param

import io.kuark.base.support.result.IdJsonResult


class SysParamRecord: IdJsonResult<String>() {

    /** 模块 */
    var module: String? = null

    /** 参数名称 */
    var paramName: String? = null

    /** 参数值，或其国际化key */
    var paramValue: String? = null

    /** 默认参数值，或其国际化key */
    var defaultValue: String? = null

    /** 序号 */
    var seqNo: Int? = null

    /** 是否启用 */
    var active: Boolean? = null

    /** 备注 */
    var remark: String? = null

}