package org.kuark.data.cache.core

import org.kuark.config.kit.SpringKit
import org.kuark.data.cache.enums.CacheStrategy
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager


class MixCacheManager(private val strategy: CacheStrategy) : AbstractTransactionSupportingCacheManager() {

    override fun loadCaches(): MutableCollection<out Cache> {
        val caches = mutableListOf<Cache>()
        val localCacheManager = getLocalCacheManager()
        val remoteCacheManager = getRemoteCacheManager()
        when (strategy) {
            CacheStrategy.SINGLE_LOCAL -> {
                localCacheManager.cacheNames.forEach { name ->
                    val localCache = localCacheManager.getCache(name)
                    caches.add(MixCache(strategy, localCache, null))
                }
            }
            CacheStrategy.REMOTE -> {
                remoteCacheManager.cacheNames.forEach { name ->
                    val remoteCache = remoteCacheManager.getCache(name)
                    caches.add(MixCache(strategy, null, remoteCache))
                }
            }
            CacheStrategy.LOCAL_REMOTE -> {
                localCacheManager.cacheNames.forEach { name ->
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

    private fun getLocalCacheManager(): CacheManager = SpringKit.getBean("localCacheManager") as CacheManager

    private fun getRemoteCacheManager(): CacheManager = SpringKit.getBean("remoteCacheManager") as CacheManager

}