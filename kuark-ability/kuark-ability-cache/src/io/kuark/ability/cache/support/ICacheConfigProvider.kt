package io.kuark.ability.cache.support

/**
 * 缓存配置提供者接口
 *
 * @author K
 * @since 1.0.0
 */
interface ICacheConfigProvider {

    /**
     * 返回指定名称的缓存配置
     *
     * @param name 缓存名称
     * @return 缓存配置信息，找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun getCacheConfig(name: String): CacheConfig?

    /**
     * 返回所有缓存配置信息
     *
     * @return Map(String, 缓存配置信息)
     * @author K
     * @since 1.0.0
     */
    fun getAllCacheConfigs(): Map<String, CacheConfig>

    /**
     * 返回本地(第一级)缓存配置信息
     *
     * @return Map(String, 本地缓存配置信息)
     * @author K
     * @since 1.0.0
     */
    fun getLocalCacheConfigs(): Map<String, CacheConfig>

    /**
     * 返回远程(第二级)缓存配置信息
     *
     * @return Map(String, 远程缓存配置信息)
     * @author K
     * @since 1.0.0
     */
    fun getRemoteCacheConfigs(): Map<String, CacheConfig>

    /**
     * 返回本地-远程(一级和二级)缓存配置信息
     *
     * @return Map(String, 地-远程(一级和二级)缓存配置信息)
     * @author K
     * @since 1.0.0
     */
    fun getLocalRemoteCacheConfigs(): Map<String, CacheConfig>

}