package io.kuark.ability.cache.context

import io.kuark.ability.cache.core.CacheMessageListener
import io.kuark.ability.cache.core.MixCache
import io.kuark.ability.cache.core.MixCacheManager
import io.kuark.ability.data.redis.context.RedisConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

/**
 * 远程缓存(第二级缓存)springboot配置类
 *
 * @author K
 * @since 1.0.0
 */
@Configuration
@ConditionalOnBean(MixCacheConfiguration::class)
@ConditionalOnExpression("'\${cache.config.strategy}'.equals('REMOTE') || '\${cache.config.strategy}'.equals('LOCAL_REMOTE')")
open class RemoteCacheConfiguration {

    @Bean(name = ["remoteCacheManager"])
    open fun remoteCacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        var redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()

        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L)) //设置缓存的默认超时时间：30分钟
            .disableCachingNullValues()             //如果是空值，不缓存
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisConfiguration.keySerializer()))
//            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer((RedisConfiguration.valueSerializer())))

        val builder = RedisCacheManager
            .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
            .cacheDefaults(redisCacheConfiguration)

        val map: MutableMap<String, RedisCacheConfiguration> = Maps.newHashMap()
        Optional.ofNullable(customCacheProperties)
            .map { p -> p.getCustomCache() }
            .ifPresent { customCache ->
                customCache.forEach { key, cache ->
                    val cfg: RedisCacheConfiguration =
                        handleRedisCacheConfiguration(cache, defaultConfiguration)
                    map.put(key, cfg)
                }
            }
        builder.withInitialCacheConfigurations(map)

        return builder.build()
    }

    @Bean
    open fun redisMessageListenerContainer(
        redisTemplate: RedisTemplate<String, Any>, cacheManager: CacheManager
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(redisTemplate.connectionFactory!!)
        val cacheMessageListener = CacheMessageListener(redisTemplate, cacheManager as MixCacheManager)
        container.addMessageListener(cacheMessageListener, ChannelTopic(MixCache.MSG_CHANNEL))
//        container.addMessageListener(RedisKeyExpirationListener(container), PatternTopic("__keyevent@*__:expired"))
        return container
    }

//    @Bean
//    @ConditionalOnMissingBean
//    open fun redisConnectionFactory(): RedisConnectionFactory = JedisConnectionFactory()

}