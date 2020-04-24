package org.kuark.data.cache.context

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.CaffeineSpec
import org.kuark.config.annotation.ConfigValue
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@ConditionalOnBean(MixCacheConfiguration::class)
@ConditionalOnExpression("'\${cache.config.strategy}'.equals('SINGLE_LOCAL') || '\${cache.config.strategy}'.equals('LOCAL_REMOTE')")
open class LocalCacheConfiguration {

    @ConfigValue("\${spring.cache.caffeine.spec}")
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