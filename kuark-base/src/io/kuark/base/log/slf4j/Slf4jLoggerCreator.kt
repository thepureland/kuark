package io.kuark.base.log.slf4j

import io.kuark.base.log.Log
import io.kuark.base.log.LogCreator
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * slf4j日志记录器创建者
 *
 * @author K
 * @since 1.0.0
 */
class Slf4jLoggerCreator : LogCreator {

    override fun createLog(clazz: KClass<*>): Log = Slf4jLogger(LoggerFactory.getLogger(clazz.java))

}