package io.kuark.base.log

import kotlin.reflect.KClass

/**
 * 日志记录器创建者
 *
 * @author K
 * @since 1.0.0
 */
interface LogCreator {

    /**
     * 创建日志记录器
     *
     * @param clazz 日志记录器所处的类
     * @return 日志记录器
     * @author K
     * @since 1.0.0
     */
    fun createLog(clazz: KClass<*>): Log

}