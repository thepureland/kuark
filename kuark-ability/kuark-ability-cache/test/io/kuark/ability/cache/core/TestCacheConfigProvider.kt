package io.kuark.ability.cache.core

import io.kuark.ability.cache.enums.CacheStrategy
import io.kuark.ability.cache.support.CacheConfig
import io.kuark.ability.cache.support.ICacheConfigProvider
import org.springframework.stereotype.Component

@Component
class TestCacheConfigProvider: ICacheConfigProvider {

    private val CACHE_NAME = "test"

    private val cacheConfig = CacheConfig().apply {
        name = "test"
        strategyDictCode = CacheStrategy.LOCAL_REMOTE.name
    }

    override fun getCacheConfig(name: String): CacheConfig {
        return cacheConfig
    }

    override fun getAllCacheConfigs(): Map<String, CacheConfig> {
        return mapOf(CACHE_NAME to cacheConfig)
    }

    override fun getLocalCacheConfigs(): Map<String, CacheConfig> {
        return mapOf(CACHE_NAME to cacheConfig)
    }

    override fun getRemoteCacheConfigs(): Map<String, CacheConfig> {
        return mapOf(CACHE_NAME to cacheConfig)
    }

    override fun getLocalRemoteCacheConfigs(): Map<String, CacheConfig> {
        return mapOf(CACHE_NAME to cacheConfig)
    }


}