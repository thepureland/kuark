package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.enums.CacheStrategy
import io.kuark.ability.cache.support.CacheConfig
import io.kuark.ability.cache.support.ICacheConfigProvider
import io.kuark.base.support.Consts
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysCacheBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component


/**
 * 缓存配置提供者
 *
 * @author K
 * @since 1.0.0
 */
@Component
@DependsOn(value = ["dataSource","springKit"])
open class CacheConfigProvider : ICacheConfigProvider {

    @Autowired
    private lateinit var sysCacheBiz: ISysCacheBiz

    private var cacheConfigs: List<CacheConfig>? = null

    private fun getCacheConfigs(): List<CacheConfig> {
        if (cacheConfigs == null) {
            val searchPayload = ListSearchPayload().apply {
                returnEntityClass = CacheConfig::class
            }

            @Suppress(Consts.Suppress.UNCHECKED_CAST)
            cacheConfigs = sysCacheBiz.search(searchPayload) as List<CacheConfig>
        }
        return cacheConfigs ?: emptyList()
    }

    override fun getCacheConfig(name: String): CacheConfig? {
        return getAllCacheConfigs()[name]
    }

    override fun getAllCacheConfigs(): Map<String, CacheConfig> {
        return getCacheConfigs().associateBy { it.name }
    }

    override fun getLocalCacheConfigs(): Map<String, CacheConfig> {
        return getCacheConfigs().filter { it.strategyDictCode == CacheStrategy.SINGLE_LOCAL.name }.associateBy { it.name }
    }

    override fun getRemoteCacheConfigs(): Map<String, CacheConfig> {
        return getCacheConfigs().filter { it.strategyDictCode == CacheStrategy.REMOTE.name }.associateBy { it.name }
    }

    override fun getLocalRemoteCacheConfigs(): Map<String, CacheConfig> {
        return getCacheConfigs().filter { it.strategyDictCode == CacheStrategy.LOCAL_REMOTE.name }.associateBy { it.name }
    }

}