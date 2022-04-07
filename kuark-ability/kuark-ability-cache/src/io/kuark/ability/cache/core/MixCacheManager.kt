package io.kuark.ability.cache.core

import io.kuark.ability.cache.context.MixCacheConfiguration
import io.kuark.ability.cache.enums.CacheStrategy
import io.kuark.ability.cache.support.ICacheConfigProvider
import io.kuark.base.log.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
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
@ConditionalOnBean(MixCacheConfiguration::class)
class MixCacheManager : AbstractTransactionSupportingCacheManager() {

    @Value("\${cache.config.strategy}")
    private var strategyStr: String? = null

    @Value("\${cache.config.enabled}")
    private var cacheEnabled: Boolean? = null

    @Autowired(required = false)
    @Qualifier("localCacheManager")
    private lateinit var localCacheManager: CacheManager
    @Autowired(required = false)
    @Qualifier("remoteCacheManager")
    private lateinit var remoteCacheManager: CacheManager
    @Autowired
    private lateinit var cacheConfigProvider: ICacheConfigProvider

    private val log = LogFactory.getLog(this::class)

    override fun loadCaches(): MutableCollection<out Cache> {
        val caches = mutableListOf<Cache>()

        // 单节点本地缓存
        val localCacheConfigs = cacheConfigProvider.getLocalCacheConfigs()
        if (localCacheConfigs.isNotEmpty()) {
            localCacheConfigs.forEach {
                val localCache = localCacheManager.getCache(it.key)
                caches.add(MixCache(CacheStrategy.SINGLE_LOCAL, localCache, null))
            }

        }

        // 远程缓存
        val remoteCacheConfigs = cacheConfigProvider.getRemoteCacheConfigs()
        if (remoteCacheConfigs.isNotEmpty()) {
            remoteCacheConfigs.forEach {
                val remoteCache = remoteCacheManager.getCache(it.key)
                caches.add(MixCache(CacheStrategy.REMOTE, null, remoteCache))
            }
        }

        // 本地-远程两级联动缓存
        val localRemoteCacheConfigs = cacheConfigProvider.getLocalRemoteCacheConfigs()
        if (localRemoteCacheConfigs.isNotEmpty()) {
            localRemoteCacheConfigs.forEach {
                val localCache = localCacheManager.getCache(it.key)
                val remoteCache = remoteCacheManager.getCache(it.key)
                caches.add(MixCache(CacheStrategy.LOCAL_REMOTE, localCache, remoteCache))
            }
        }

        return caches
    }

    fun clearLocal(cacheName: String, key: Any?) {
        val cache = getCache(cacheName) ?: return
        val mixCache = cache as MixCache
        mixCache.clearLocal(key)
    }

    fun isCacheEnabled(): Boolean = cacheEnabled == true

    override fun initializeCaches() {
        super.initializeCaches()

    }

}