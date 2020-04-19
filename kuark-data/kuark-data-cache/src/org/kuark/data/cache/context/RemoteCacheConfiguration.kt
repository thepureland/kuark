package org.kuark.data.cache.context

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@ConditionalOnProperty(prefix = "cache.config", name = ["enabled"], value = ["", ""], matchIfMissing = false)
@ConditionalOnExpression("\${cache.config.strategy:REMOTE} || \${cache.config.strategy:LOCAL_REMOTE}")
open class RemoteCacheConfiguration {

    @Bean(name = ["redisTemplate"])
    open fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.keySerializer = keySerializer()
        redisTemplate.hashKeySerializer = keySerializer()
        redisTemplate.valueSerializer = valueSerializer()
        redisTemplate.hashValueSerializer = valueSerializer()
        return redisTemplate;
    }

    @Bean(name = ["remoteCacheManager"])
    open fun remoteCacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        var redisCacheConfiguration = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()

        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L)) //设置缓存的默认超时时间：30分钟
            .disableCachingNullValues()             //如果是空值，不缓存
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))         //设置key序列化器
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer((valueSerializer())));  //设置value序列化器

        return RedisCacheManager
            .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
            .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean
    @ConditionalOnMissingBean
    open fun redisConnectionFactory(): RedisConnectionFactory = JedisConnectionFactory()

    private fun keySerializer(): RedisSerializer<String> = StringRedisSerializer()

    private fun valueSerializer(): RedisSerializer<Any> = GenericJackson2JsonRedisSerializer()

}