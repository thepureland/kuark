package io.kuark.data.redis.context

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * redis的spring boot配置类
 *
 * @author K
 * @since 1.0.0
 */
@Configuration
open class RedisConfiguration {

    @Primary
    @Bean(name = ["redisTemplate"])
    open fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any?> {
        val redisTemplate = RedisTemplate<String, Any?>()
        redisTemplate.setConnectionFactory(redisConnectionFactory) // 不能用属性直接赋值的方式，会报Val cannot be reassigned
        redisTemplate.keySerializer = keySerializer()
        redisTemplate.hashKeySerializer = keySerializer()
//        redisTemplate.valueSerializer = valueSerializer()
//        redisTemplate.hashValueSerializer = valueSerializer()
        return redisTemplate
    }

    companion object {

        fun keySerializer(): RedisSerializer<String> = StringRedisSerializer()

        fun valueSerializer(): RedisSerializer<Any> {
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

}