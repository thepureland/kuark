package org.kuark.data.cache.core

import org.kuark.base.log.LoggerFactory
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate

class CacheMessageListener(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val mixCacheManager: MixCacheManager
) : MessageListener {

    private val logger = LoggerFactory.getLogger(CacheMessageListener::class)

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val cacheMessage = redisTemplate.valueSerializer.deserialize(message.body) as CacheMessage
        mixCacheManager.clearLocal(cacheMessage.cacheName, cacheMessage.key)
    }

}