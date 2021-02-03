package io.kuark.ability.cache.core

import io.kuark.ability.cache.enums.CacheStrategy
import io.kuark.context.spring.SpringKit
import org.springframework.cache.Cache
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.Callable

/**
 * 混合缓存(两级缓存: 本地+远程)
 *
 * @author K
 * @since 1.0.0
 */
class MixCache(
    private val strategy: CacheStrategy,
    private val localCache: Cache?,
    private val remoteCache: Cache?
) : Cache {

    companion object {
        const val MSG_CHANNEL = "cache:local-remote:channel"
    }

    override fun clear() {
        when (strategy) {
            CacheStrategy.SINGLE_LOCAL -> localCache?.clear()
            CacheStrategy.REMOTE -> remoteCache?.clear()
            CacheStrategy.LOCAL_REMOTE -> {
                remoteCache?.clear()
                pushMsg(name, null)
            }
        }
    }

    override fun put(key: Any, value: Any?) {
        when (strategy) {
            CacheStrategy.SINGLE_LOCAL -> localCache?.put(key, value)
            CacheStrategy.REMOTE -> remoteCache?.put(key, value)
            CacheStrategy.LOCAL_REMOTE -> {
                remoteCache?.put(key, value)
                pushMsg(name, key) //TODO 异步?
//                localCache?.put(key, value)
            }
        }
    }

    override fun getName(): String =
        when (strategy) {
            CacheStrategy.SINGLE_LOCAL, CacheStrategy.LOCAL_REMOTE -> localCache!!.name
            CacheStrategy.REMOTE -> remoteCache!!.name
        }

    override fun getNativeCache(): Any = this

    override fun get(key: Any): Cache.ValueWrapper? =
        when (strategy) {
            CacheStrategy.SINGLE_LOCAL -> localCache!!.get(key)
            CacheStrategy.REMOTE -> remoteCache!!.get(key)
            CacheStrategy.LOCAL_REMOTE -> {
                var value = localCache!!.get(key)
                if (value?.get() == null) {
                    value = remoteCache!!.get(key)
                    localCache.put(key, value?.get())
                }
                value
            }
        }

    override fun <T : Any?> get(key: Any, type: Class<T>?): T? =
        when (strategy) {
            CacheStrategy.SINGLE_LOCAL -> localCache!!.get(key, type)
            CacheStrategy.REMOTE -> remoteCache!!.get(key, type)
            CacheStrategy.LOCAL_REMOTE -> {
                var value = localCache!!.get(key, type)
                if (value == null) {
                    value = remoteCache!!.get(key, type)
                    localCache.put(key, value)
                }
                value
            }
        }

    override fun <T : Any?> get(key: Any, valueLoader: Callable<T>): T? =
        when (strategy) {
            CacheStrategy.SINGLE_LOCAL -> localCache!!.get(key, valueLoader)
            CacheStrategy.REMOTE -> remoteCache!!.get(key, valueLoader)
            CacheStrategy.LOCAL_REMOTE -> {
                var value = localCache!!.get(key, valueLoader)
                if (value == null) {
                    value = remoteCache!!.get(key, valueLoader)
                    localCache.put(key, value)
                }
                value
            }
        }

    override fun evict(key: Any) {
        when (strategy) {
            CacheStrategy.SINGLE_LOCAL -> localCache?.evict(key)
            CacheStrategy.REMOTE -> remoteCache?.evict(key)
            CacheStrategy.LOCAL_REMOTE -> {
                remoteCache?.evict(key)
                pushMsg(name, key)
            }
        }
    }

    private fun pushMsg(name: String, key: Any?) {
        val redisTemplate = SpringKit.getBean(RedisTemplate::class)
        val msg = CacheMessage(name, key)
        redisTemplate.convertAndSend(MSG_CHANNEL, msg)
    }

    fun clearLocal(key: Any?) {
        if (key == null) {
            localCache?.clear()
        } else {
            localCache?.evict(key)
        }
    }

}