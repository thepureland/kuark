package io.kuark.ability.web.common

/**
 * 返回给前端的统一结果
 *
 * @author K
 * @since 1.0.0
 */
data class WebResult<T>(
    var data: T?,
    var message: String = "",
    var code: Int = 200
) {

    constructor(data: T?): this(data, "", 200)

    constructor(message: String): this(null, message, 200)

    constructor(message: String, code: Int): this(null, message, code)

}