package org.kuark.base.log.slf4j

import org.kuark.base.log.Log
import org.slf4j.spi.LocationAwareLogger
import java.text.MessageFormat

/**
 * slf4j实现的日志记录器
 *
 * @author K
 * @since 1.0.0
 */
class Slf4jLogger : Log {

    private var logger: LocationAwareLogger
    private val FQCN: String = this::class.java.name

    constructor(logger: org.slf4j.Logger) {
        this.logger = logger as LocationAwareLogger
    }


    override fun trace(msg: String?, vararg args: Any?) {
        if (logger.isTraceEnabled) // 提前判断是为了减少可能不必要的字符串模板填充机会
            logger.log(null, FQCN, LocationAwareLogger.TRACE_INT, getMsg(msg, *args), null, null)
    }

    override fun debug(msg: String?, vararg args: Any?) {
        if (logger.isDebugEnabled) // 提前判断是为了减少可能不必要的字符串模板填充机会
            logger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, getMsg(msg, *args), null, null)
    }

    override fun info(msg: String?, vararg args: Any?) =
//        if (log.isInfoEnabled) // 不再像trace和debug方法那样提前判断，是因为正常情况下info级别都会打开，
//                               // 提前了大部分情况下反而是多余的计算，warn和error同理
        logger.log(null, FQCN, LocationAwareLogger.INFO_INT, getMsg(msg, *args), null, null)

    override fun warn(msg: String?, vararg args: Any?) =
        logger.log(null, FQCN, LocationAwareLogger.INFO_INT, getMsg(msg, *args), null, null)

    override fun error(msg: String?, vararg args: Any?) =
        logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, getMsg(msg, *args), null, null)

    override fun error(e: Throwable, msg: String?, vararg args: Any?) =
        logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, getMsg(msg, *args), null, e)

    override fun error(e: Throwable) =
        logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, getMsg(e.message), null, e)

    private fun getMsg(msg: String?, vararg args: Any?): String? {
        return if (args.isNotEmpty()) {
            MessageFormat.format(msg, *args)
        } else msg
    }

    override fun isTraceEnabled(): Boolean = logger.isTraceEnabled

    override fun isDebugEnabled(): Boolean = logger.isDebugEnabled

    override fun isInfoEnabled(): Boolean = logger.isInfoEnabled

    override fun isWarnEnabled(): Boolean = logger.isWarnEnabled

    override fun isErrorEnabled(): Boolean = logger.isErrorEnabled

}