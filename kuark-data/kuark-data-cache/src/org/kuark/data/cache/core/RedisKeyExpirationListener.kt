package org.kuark.data.cache.core

import org.kuark.base.log.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component

@Component
class RedisKeyExpirationListener(
    @Autowired listenerContainer: RedisMessageListenerContainer,
    @Autowired private val mixCacheManager: MixCacheManager) :
    KeyExpirationEventMessageListener(listenerContainer) {

    private val logger = LoggerFactory.getLogger(this::class)

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val expiredKeys = message.toString() // 获取失效的key
        val parts = expiredKeys.split("::")
        val cacheName = parts[0]
        val key = parts[1]
        mixCacheManager.clearLocal(cacheName, key)
        logger.info("远程缓存已失效，同步清除其本地缓存：$expiredKeys")
    }

}