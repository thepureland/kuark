package io.kuark.ability.cache.support

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberProperties

/**
 * 缓存名称处理，用来获取所有实现ICacheNames接口的bean定义的缓存名称。
 *
 * @author K
 * @since 1.0.0
 */
@Component
class CacheNameResolver: ApplicationContextAware {

    private lateinit var applicationContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    fun resolve(): Set<String> {
        val beans = applicationContext.getBeansOfType(ICacheNames::class.java)
        val cacheNames = mutableSetOf<String>()
        val stringType = String::class.createType()
        for (bean in beans.values) {
            bean::class.declaredMemberProperties
                .filter { it.isConst && it.returnType == stringType }
                .forEach { cacheNames.add(it.call() as String) }
        }
        return cacheNames
    }

}