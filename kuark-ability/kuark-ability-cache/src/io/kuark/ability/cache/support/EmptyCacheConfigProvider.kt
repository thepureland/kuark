package io.kuark.ability.cache.support


/**
 * 空实现的缓存配置提供者。使用者如果需要缓存功能，需要提供自己的ICacheConfigProvider实现
 *
 * @author K
 * @since 1.0.0
 */
class EmptyCacheConfigProvider : ICacheConfigProvider {

    override fun getCacheConfig(name: String): CacheConfig? = null

    override fun getAllCacheConfigs(): Map<String, CacheConfig> = emptyMap()

    override fun getLocalCacheConfigs(): Map<String, CacheConfig> = emptyMap()

    override fun getRemoteCacheConfigs(): Map<String, CacheConfig> = emptyMap()

    override fun getLocalRemoteCacheConfigs(): Map<String, CacheConfig> = emptyMap()

}