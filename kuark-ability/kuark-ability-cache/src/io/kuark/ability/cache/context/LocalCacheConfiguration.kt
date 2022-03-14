package io.kuark.ability.cache.context

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.CaffeineSpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean

/**
 * 本地缓存(第一级缓存)springboot配置类
 *
 * @author K
 * @since 1.0.0
 */
@SpringBootConfiguration
@ConditionalOnBean(MixCacheConfiguration::class)
@ConditionalOnExpression("'\${cache.config.strategy}'.equals('SINGLE_LOCAL') || '\${cache.config.strategy}'.equals('LOCAL_REMOTE')")
open class LocalCacheConfiguration {

    @Value("\${spring.cache.caffeine.spec}")
    private lateinit var caffeineSpec: String

    @Bean(name = ["localCacheManager"])
    open fun localCacheManager(): CacheManager {
        val cacheManager = CaffeineCacheManager()
        val spec = CaffeineSpec.parse(caffeineSpec)
        val caffeine = Caffeine.from(spec)
        cacheManager.setCaffeine(caffeine)
        return cacheManager
    }

}