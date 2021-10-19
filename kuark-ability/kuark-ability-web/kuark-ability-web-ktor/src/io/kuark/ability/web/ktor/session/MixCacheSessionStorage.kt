package io.kuark.ability.web.ktor.session

import io.ktor.sessions.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.cache.context.MixCacheConfiguration
import io.kuark.ability.cache.core.MixCacheManager
import io.kuark.ability.cache.kit.CacheKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

/**
 * 基于两级缓存的会话存储
 *
 * @author K
 * @since 1.0.0
 */
@Component
@ConditionalOnBean(MixCacheConfiguration::class)
class MixCacheSessionStorage : SessionStorage {

    @Autowired
    private lateinit var cacheManager: MixCacheManager

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    override suspend fun invalidate(id: String) {
        CacheKit.evict(CacheNames.SESSION, id)
    }

    override suspend fun <R> read(id: String, consumer: suspend (ByteReadChannel) -> R): R {
        val value = CacheKit.getValue(CacheNames.SESSION, id, ByteArray::class)
        return value?.let { data -> consumer(ByteReadChannel(data)) }
            ?: throw NoSuchElementException("Session $id not found")
    }

    override suspend fun write(id: String, provider: suspend (ByteWriteChannel) -> Unit) {
        coroutineScope {
            val channel = writer(Dispatchers.Unconfined, autoFlush = true) {
                provider(channel)
            }.channel
            CacheKit.put(CacheNames.SESSION, id, channel.toByteArray())
        }
    }

}