package io.kuark.context.spring

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

/**
 * spring工具类
 *
 * @author K
 * @since 1.0.0
 */
@Component
object SpringKit : ApplicationContextAware {

    private lateinit var applicationContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        SpringKit.applicationContext = applicationContext
    }

    fun getApplicationContext(): ApplicationContext =
        applicationContext

    fun getBean(beanName: String): Any = applicationContext.getBean(beanName)

    fun <T : Any> getBean(beanClass: KClass<T>): T = applicationContext.getBean(beanClass.java)

    fun getProperty(propertyName: String): String? = applicationContext.environment.getProperty(propertyName)

}