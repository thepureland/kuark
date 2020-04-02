package org.kuark.base.log

import kotlin.reflect.KClass

/**
 * 日志记录器创建者
 * @since 1.0.0
 */
interface LoggerCreator {

    /**
     * 创建日志记录器
     * @param clazz 日志记录器所处的类
     * @return 日志记录器
     * @since 1.0.0
     */
    fun createLogger(clazz: KClass<*>): Logger

}