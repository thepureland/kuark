package org.kuark.config.kit

import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass


object SpringKit {

    lateinit var applicationContext: ApplicationContext

    fun getBean(beanName: String): Any {
        checkApplicationContext()
        return applicationContext.getBean(beanName)
    }

    fun <T : Any> getBean(beanClass: KClass<T>): T {
        checkApplicationContext()
        return applicationContext.getBean(beanClass.java)
    }

    fun getProperty(propertyName: String): String? = applicationContext.environment.getProperty(propertyName)

    /**
     * 检查 ApplicationContext 是否注入
     */
    private inline fun checkApplicationContext() = checkNotNull(applicationContext) { "spring applicaitonContext未注入" }

}