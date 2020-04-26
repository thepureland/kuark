package org.kuark.cache.context

import org.kuark.cache.core.CacheMessageListener
import org.kuark.cache.core.MixCache
import org.kuark.cache.core.MixCacheManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@Configuration
@ConditionalOnBean(MixCacheConfiguration::class)
@ConditionalOnExpression("'\${cache.config.strategy}'.equals('REMOTE') || '\${cache.config.strategy}'.equals('LOCAL_REMOTE')")
open class RemoteCacheConfiguration {

    @Primary
    @Bean(name = ["redisTemplate"])
    open fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.keySerializer = keySerializer()
        redisTemplate.hashKeySerializer = keySerializer()
        redisTemplate.valueSerializer = valueSerializer()
        redisTemplate.hashValueSerializer = valueSerializer()
        return redisTemplate
    }

    @Bean(name = ["remoteCacheManager"])
    open fun remoteCacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        var redisCacheConfiguration = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()

        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L)) //设置缓存的默认超时时间：30分钟
            .disableCachingNullValues()             //如果是空值，不缓存
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))         //设置key序列化器
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer((valueSerializer())))  //设置value序列化器

        return RedisCacheManager
            .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
            .cacheDefaults(redisCacheConfiguration).build()
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

    private fun keySerializer(): RedisSerializer<String> = StringRedisSerializer()

    private fun valueSerializer(): RedisSerializer<Any> {
         return GenericJackson2JsonRedisSerializer()

        // alibaba的fastjson
//        val serializer = Jackson2JsonRedisSerializer(Any::class.java)
//        val mapper = ObjectMapper()
//        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
//        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL)
//        serializer.setObjectMapper(mapper)
//        return serializer
    }

}