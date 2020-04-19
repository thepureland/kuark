package org.kuark.data.cache.context

import org.kuark.config.annotation.ConfigValue
import org.kuark.data.cache.core.CacheMessageListener
import org.kuark.data.cache.core.MixCache
import org.kuark.data.cache.core.MixCacheManager
import org.kuark.data.cache.enums.CacheStrategy
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.cache.interceptor.CacheResolver
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.SimpleKeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer

@Configuration
@ConditionalOnProperty(prefix = "cache.config", name = ["enabled"], havingValue = "true", matchIfMissing = false)
@ComponentScan(basePackages = ["org.kuark"])
// 使用@EnableCaching启用Cache注解支持
@EnableCaching(proxyTargetClass = true)
open class MixCacheConfiguration : CachingConfigurer {

    @ConfigValue("\${cache.config.strategy}")
    private lateinit var strategy: String

    @Bean
    override fun cacheManager(): CacheManager = MixCacheManager(CacheStrategy.valueOf(strategy))

    @Bean
    open fun redisMessageListenerContainer(
        redisTemplate: RedisTemplate<String, Any>, cacheManager: CacheManager
    ): RedisMessageListenerContainer {
        val redisMessageListenerContainer = RedisMessageListenerContainer()
        redisMessageListenerContainer.setConnectionFactory(redisTemplate.connectionFactory!!)
        val cacheMessageListener = CacheMessageListener(redisTemplate, cacheManager)
        redisMessageListenerContainer.addMessageListener(cacheMessageListener, ChannelTopic(MixCache.MSG_CHANNEL))
        return redisMessageListenerContainer
    }

    @Bean
    override fun keyGenerator(): KeyGenerator = SimpleKeyGenerator()

    override fun cacheResolver(): CacheResolver {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun errorHandler(): CacheErrorHandler {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}