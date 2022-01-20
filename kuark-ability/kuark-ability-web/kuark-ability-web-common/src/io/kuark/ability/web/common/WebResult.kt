package io.kuark.ability.web.common

import io.kuark.base.support.result.IJsonResult

/**
 * 返回给前端的统一结果
 *
 * @author K
 * @since 1.0.0
 */
data class WebResult<T>(
    var data: T?,
    var msg: String?,
    var code: Int = 200
): IJsonResult {

    constructor() : this(null, null, 200)

    constructor(data: T?, msg: String? = null) : this(data, msg, 200)

    constructor(msg: String?, code: Int) : this(null, msg, code)

}