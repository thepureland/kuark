package io.kuark.ability.cache.context

import io.kuark.ability.cache.core.CacheMessageListener
import io.kuark.ability.cache.core.MixCache
import io.kuark.ability.cache.core.MixCacheManager
import io.kuark.ability.cache.support.ICacheConfigProvider
import io.kuark.ability.data.redis.context.RedisConfiguration
import io.kuark.base.log.LogFactory
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
//@ConditionalOnExpression("'\${cache.config.strategy}'.equals('REMOTE') || '\${cache.config.strategy}'.equals('LOCAL_REMOTE')")
open class RemoteCacheConfiguration {

    private val log = LogFactory.getLog(this::class)

    @Bean(name = ["remoteCacheManager"])
    open fun remoteCacheManager(
        redisConnectionFactory: RedisConnectionFactory,
        cacheConfigProvider: ICacheConfigProvider
    ): CacheManager {
        val remoteCacheConfigs = cacheConfigProvider.getRemoteCacheConfigs()
        val localRemoteCacheConfigs = cacheConfigProvider.getLocalRemoteCacheConfigs()
        val cacheConfigs = remoteCacheConfigs.plus(localRemoteCacheConfigs)
        val configMap = cacheConfigs.values.associate {
            log.info("初始化远程缓存【${it.name}】...")
            val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()             //如果是空值，不缓存
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisConfiguration.keySerializer()))
//              .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer((RedisConfiguration.valueSerializer())))
            if (it.ttl != null) {
                redisCacheConfiguration.entryTtl(Duration.ofSeconds(it.ttl!!.toLong()))
            }
            log.info("初始化远程缓存【${it.name}】成功！")
            it.name to redisCacheConfiguration
        }

        return RedisCacheManager
            .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
            .initialCacheNames(configMap.keys)
            .withInitialCacheConfigurations(configMap)
            .build()
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