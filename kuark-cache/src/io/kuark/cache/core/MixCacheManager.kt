package io.kuark.cache.core

import io.kuark.base.log.LogFactory
import io.kuark.cache.context.CacheNameResolver
import io.kuark.cache.enums.CacheStrategy
import io.kuark.context.annotation.ConfigValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

/**
 * 混合缓存(两级缓存: 本地+远程)管理器
 *
 * @author K
 * @since 1.0.0
 */
@Primary
@Component("cacheManager")
class MixCacheManager : AbstractTransactionSupportingCacheManager() {

    @ConfigValue("\${cache.config.strategy}")
    private var strategyStr: String? = null

    @Autowired(required = false)
    @Qualifier("localCacheManager")
    private lateinit var localCacheManager: CacheManager
    @Autowired(required = false)
    @Qualifier("remoteCacheManager")
    private lateinit var remoteCacheManager: CacheManager
    @Autowired
    private lateinit var cacheNameResolver: CacheNameResolver

    private val log = LogFactory.getLog(this::class)

    override fun loadCaches(): MutableCollection<out Cache> {
        val caches = mutableListOf<Cache>()
        val strategy = try {
            CacheStrategy.valueOf(strategyStr!!)
        } catch (e: Exception) {
            return caches
        }
        val cacheNames = cacheNameResolver.resolve()
        when (strategy) {
            CacheStrategy.SINGLE_LOCAL -> {
                log.info("缓存策略为【单节点本地缓存】")
                cacheNames.forEach { name ->
                    val localCache = localCacheManager.getCache(name)
                    caches.add(MixCache(strategy, localCache, null))
                }
            }
            CacheStrategy.REMOTE -> {
                log.info("缓存策略为【远程缓存】")
                cacheNames.forEach { name ->
                    val remoteCache = remoteCacheManager.getCache(name)
                    caches.add(MixCache(strategy, null, remoteCache))
                }
            }
            CacheStrategy.LOCAL_REMOTE -> {
                log.info("缓存策略为【本地-远程两级联动缓存】")
                cacheNames.forEach { name ->
                    val localCache = localCacheManager.getCache(name)
                    val remoteCache = remoteCacheManager.getCache(name)
                    caches.add(MixCache(strategy, localCache, remoteCache))
                }
            }
        }
        return caches
    }

    fun clearLocal(cacheName: String, key: Any?) {
        val cache = getCache(cacheName) ?: return
        val mixCache = cache as MixCache
        mixCache.clearLocal(key)
    }

}