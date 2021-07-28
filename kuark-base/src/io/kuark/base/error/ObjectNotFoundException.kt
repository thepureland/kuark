package io.kuark.base.error

/**
 * 对象找不到异常。
 * 用于本该找得到，但结果未找到的情况。
 *
 * @author K
 * @since 1.0.0
 */
class ObjectNotFoundException(
    override val message: String?,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    constructor(e: Throwable) : this(e.message, e)

}