package io.kuark.base.net.http

/**
 * http调用结果
 *
 * @author K
 * @since 1.0.0
 */
class HttpResult(val status: Int, val msg: String, val data: Any? = null) {

    companion object {
        fun ok(msg: String, data: Any? = null): HttpResult {
            return HttpResult(200, msg, data)
        }

        fun error(msg: String, data: Any? = null): HttpResult {
            return HttpResult(500, msg, data)
        }
    }

}