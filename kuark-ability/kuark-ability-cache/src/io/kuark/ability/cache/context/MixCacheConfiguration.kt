package io.kuark.ability.cache.context

import io.kuark.ability.cache.core.DefaultKeysGenerator
import io.kuark.ability.cache.core.MixCacheManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.cache.interceptor.CacheResolver
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.cache.interceptor.SimpleKeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * 混合缓存(两级缓存: 本地+远程)springboot配置类
 *
 * @author K
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "cache.config", name = ["enabled"], havingValue = "true", matchIfMissing = false)
@ComponentScan(basePackages = ["io.kuark"])
@EnableCaching(proxyTargetClass = true)
open class MixCacheConfiguration : CachingConfigurer {

    @Autowired
    private lateinit var cacheManager: MixCacheManager

    override fun cacheManager(): CacheManager {
        return cacheManager
    }

    @Bean
    override fun keyGenerator(): KeyGenerator = SimpleKeyGenerator()

    @Bean
    open fun keysGenerator(): DefaultKeysGenerator = DefaultKeysGenerator()

    override fun cacheResolver(): CacheResolver? {
        return null
    }

    override fun errorHandler(): CacheErrorHandler? {
        return null
    }

}