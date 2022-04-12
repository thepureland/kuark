package io.kuark.ability.cache.core

import io.kuark.base.log.LogFactory
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate

/**
 * 缓存通知消息监听器
 *
 * @author K
 * @since 1.0.0
 */
class CacheMessageListener(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val mixCacheManager: MixCacheManager
) : MessageListener {

    private val logger = LogFactory.getLog(this::class)

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            logger.info("收到清除本地缓存的通知")
            val cacheMessage = redisTemplate.valueSerializer.deserialize(message.body) as CacheMessage
            mixCacheManager.clearLocal(cacheMessage.cacheName, cacheMessage.key)
            logger.info("清除本地缓存：${cacheMessage.cacheName}::${cacheMessage.key}")
        } catch (e: Throwable) {
            e.printStackTrace()
            logger.error(e)
        }
    }

}