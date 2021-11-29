package io.kuark.service.sys.common.vo.reg.param

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

/**
 * 系统参数查询条件载体
 *
 * @author K
 * @since 1.0.0
 */
class RegParamSearchPayload: ListSearchPayload() {

    /** 模块 */
    var module: String? = null

    /** 参数名称 */
    var paramName: String? = null

    /** 参数值，或其国际化key */
    var paramValue: String? = null

    /** 是否启用 */
    var active: Boolean? = null

    override var returnEntityClass: KClass<*>? = RegParamRecord::class

}