package org.kuark.base.log.slf4j

import org.kuark.base.log.Logger
import org.kuark.base.log.LoggerCreator
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * slf4j日志记录器创建者
 * @since 1.0.0
 */
class Slf4jLoggerCreator : LoggerCreator {

    override fun createLogger(clazz: KClass<*>): Logger = Slf4jLogger(LoggerFactory.getLogger(clazz.java))

}