package io.kuark.cache.core

import io.kuark.base.log.LogFactory
import io.kuark.cache.context.LocalCacheConfiguration
import io.kuark.cache.context.RemoteCacheConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component

/**
 * redis缓存key过期监听器
 *
 * @author K
 * @since 1.0.0
 */
@Component
@ConditionalOnBean(value = [LocalCacheConfiguration::class, RemoteCacheConfiguration::class])
class RedisKeyExpirationListener(
    @Autowired listenerContainer: RedisMessageListenerContainer,
    @Autowired private val mixCacheManager: MixCacheManager
) : KeyExpirationEventMessageListener(listenerContainer) {

    private val logger = LogFactory.getLog(this::class)

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val expiredKeys = message.toString() // 获取失效的key
        val parts = expiredKeys.split("::")
        val cacheName = parts[0]
        val key = parts[1]
        mixCacheManager.clearLocal(cacheName, key)
        logger.info("远程缓存已失效，同步清除其本地缓存：$expiredKeys")
    }

}