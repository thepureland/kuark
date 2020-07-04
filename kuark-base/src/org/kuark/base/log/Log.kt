package org.kuark.base.log

/**
 * 日志记录器，让使用者对实际使用的第三方的具体日志库透明
 *
 * @author K
 * @since 1.0.0
 */
interface Log {

    /**
     * 记录跟踪日志
     * @param msg 日志内容，支持java字符串模板，如果不是基于模板参数计算性能考量(想推迟计算)，请使用kotlin的字符串模板
     * @param args java字符串模板参数
     * @since 1.0.0
     */
    fun trace(msg: String, vararg args: Any?)

    /**
     * 记录调试日志
     * @param msg 日志内容，支持java字符串模板，如果不是基于模板参数计算性能考量(想推迟计算)，请使用kotlin的字符串模板
     * @param args java字符串模板参数
     * @since 1.0.0
     */
    fun debug(msg: String, vararg args: Any?)

    /**
     * 记录一般日志
     * @param msg 日志内容，支持java字符串模板，如果不是基于模板参数计算性能考量(想推迟计算)，请使用kotlin的字符串模板
     * @param args java字符串模板参数
     * @since 1.0.0
     */
    fun info(msg: String, vararg args: Any?)

    /**
     * 记录警告日志
     * @param msg 日志内容，支持java字符串模板，如果不是基于模板参数计算性能考量(想推迟计算)，请使用kotlin的字符串模板
     * @param args java字符串模板参数
     * @since 1.0.0
     */
    fun warn(msg: String, vararg args: Any?)

    /**
     * 记录错误日志
     * @param msg 日志内容，支持java字符串模板，如果不是基于模板参数计算性能考量(想推迟计算)，请使用kotlin的字符串模板
     * @param args java字符串模板参数
     * @since 1.0.0
     */
    fun error(msg: String, vararg args: Any?)

    /**
     * 记录错误日志
     * @param e 异常对象
     * @param msg 日志内容，支持java字符串模板，如果不是基于模板参数计算性能考量(想推迟计算)，请使用kotlin的字符串模板
     * @param args java字符串模板参数
     * @since 1.0.0
     */
    fun error(e: Throwable, msg: String, vararg args: Any?)

    /**
     * 记录错误日志
     * @param e 异常对象
     * @since 1.0.0
     */
    fun error(e: Throwable)

    /**
     * 是否记录跟踪级别及以上的日志
     * @since 1.0.0
     */
    fun isTraceEnabled(): Boolean

    /**
     * 是否记录调试级别及以上的日志
     * @since 1.0.0
     */
    fun isDebugEnabled(): Boolean

    /**
     * 是否记录一般级别及以上的日志
     * @since 1.0.0
     */
    fun isInfoEnabled(): Boolean

    /**
     * 是否记录警告级别及以上的日志
     * @since 1.0.0
     */
    fun isWarnEnabled(): Boolean

    /**
     * 是否记录错误级别的日志
     * @since 1.0.0
     */
    fun isErrorEnabled(): Boolean

}