package io.kuark.base.error

/**
 * 对象已存在异常
 *
 * @author K
 * @since 1.0.0
 */
class ObjectAlreadyExistsException(
    override val message: String?,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    constructor(e: Throwable) : this(e.message, e)

}