package io.kuark.base.log

import io.kuark.base.log.slf4j.Slf4jLoggerCreator
import kotlin.reflect.KClass

/**
 * 日志记录器工厂，用于创建日志记录器
 *
 * @author K
 * @since 1.0.0
 */
object LogFactory {

    private val loggerCreator: LogCreator = Slf4jLoggerCreator()

    /**
     * 获取日志记录器
     *
     * @param clazz 使用该日志记录器的类
     * @return 指定类的日志记录器
     * @author K
     * @since 1.0.0
     */
    fun getLog(clazz: KClass<*>): Log = loggerCreator.createLog(clazz)

}