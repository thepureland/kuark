package io.kuark.base.error

/**
 * 非法操作异常
 *
 * @author K
 * @since 1.0.0
 */
class IllegalOperationException (
    override val message: String?,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    constructor(e: Throwable) : this(e.message, e)

}