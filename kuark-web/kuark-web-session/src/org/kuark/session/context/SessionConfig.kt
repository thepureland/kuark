package org.kuark.session.context

import org.kuark.base.log.LogFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession
import org.springframework.session.events.SessionCreatedEvent
import org.springframework.session.events.SessionDeletedEvent
import org.springframework.session.events.SessionExpiredEvent


@Configuration
@EnableRedisHttpSession(redisNamespace = "kuark:session")
open class SessionConfig {

    private val logger = LogFactory.getLog(this::class)

    /**
     * Redis内session过期事件监听
     * @param expiredEvent
     */
    @EventListener
    fun onSessionExpired(expiredEvent: SessionExpiredEvent) {
        val sessionId = expiredEvent.sessionId
        logger.info("Session失效事件:$sessionId")
    }

    /**
     * Redis内session创建事件监听
     * @param createdEvent
     */
    @EventListener
    fun onSessionCreated(createdEvent: SessionCreatedEvent) {
        val sessionId = createdEvent.sessionId
        logger.info("创建Session事件:$sessionId")
    }

    /**
     * Redis内session删除事件监听
     * @param deletedEvent
     */
    @EventListener
    fun onSessionDeleted(deletedEvent: SessionDeletedEvent) {
        val sessionId = deletedEvent.sessionId
        logger.info("删除Session事件:$sessionId")
    }

    /**
     * 更换序列化器
     *
     * @return
     */
    @Bean("springSessionDefaultRedisSerializer")
    open fun setSerializer(): RedisSerializer<*> {
        return GenericJackson2JsonRedisSerializer()
    }

}