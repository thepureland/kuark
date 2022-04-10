package io.kuark.ability.cache.support

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import org.springframework.beans.factory.InitializingBean


/**
 * 缓存管理支持抽象类
 *
 * @author K
 * @since 1.0.0
 */
abstract class AbstractCacheManagementSupport<T> : InitializingBean {

    protected val log = LogFactory.getLog(this::class)

    /**
     * 返回缓存名称
     *
     * @return 缓存名称
     * @author K
     * @since 1.0.0
     */
    abstract fun cacheName(): String

    /**
     * 检测缓存key是否存在
     *
     * @param key 缓存的key
     * @return true: 存在于缓存中，false: 不存在
     * @author K
     * @since 1.0.0
     */
    fun isExists(key: String): Boolean {
        return value(key) == null
    }

    /**
     * 获取指定缓存key对应的值
     *
     * @param key 缓存的key
     * @return 缓存key对应的值
     * @author K
     * @since 1.0.0
     */
    fun value(key: String): T? {
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return CacheKit.getValue(cacheName(), key) as T?
    }

    /**
     * 踢除指定key的缓存
     *
     * @param key 缓存的key
     * @author K
     * @since 1.0.0
     */
    fun evict(key: String) {
        CacheKit.evict(cacheName(), key)
        log.info("手动踢除名称为${cacheName()}，key为${key}的缓存。")
    }

    /**
     * 清除所有缓存
     *
     * @author K
     * @since 1.0.0
     */
    fun clear() {
        CacheKit.clear(cacheName())
        log.info("手动清除名称为${cacheName()}的所有缓存。")
    }

    /**
     * 重载指定key的缓存
     *
     * @param key 缓存的key
     * @author K
     * @since 1.0.0
     */
    fun reload(key: String) {
        evict(key)
        log.info("手动重载名称为${cacheName()}，key为${key}的缓存...")
        val role = doReload(key)
        if (role == null) {
            log.info("数据库中已不存在对应数据！")
        } else {
            log.info("重载成功。")
        }
    }

    /**
     * 执行重载指定key的缓存
     *
     * @param key 缓存的key
     * @return 缓存key对应的值。如果找不到，集合类型返回空集合，其它的返回null。
     * @author K
     * @since 1.0.0
     */
    protected abstract fun doReload(key: String): T?

    /**
     * 重载所有缓存
     *
     * @param clear 重载前是否先清除
     * @author K
     * @since 1.0.0
     */
    abstract fun reloadAll(clear: Boolean)

    override fun afterPropertiesSet() {
        val cacheConfig = CacheKit.getCacheConfig(cacheName())
        if (cacheConfig != null && cacheConfig.writeOnBoot == true) {
            reloadAll(false)
        }
    }

}