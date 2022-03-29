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
abstract class AbstractCacheManagementSupport<T>: InitializingBean {

    protected val log = LogFactory.getLog(this::class)

    abstract fun cacheName(): String

    fun isExists(key: String): Boolean {
        return value(key) == null
    }

    abstract fun keys(): Set<String>

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun value(key: String): T? {
        return CacheKit.getValue(cacheName(), key) as T?
    }

    abstract fun values(): List<T>

    fun evict(key: String) {
        CacheKit.evict(cacheName(), key)
        log.info("手动踢除名称为${cacheName()}，key为${key}的缓存。")
    }

    fun clear() {
        CacheKit.clear(cacheName())
        log.info("手动清除名称为${cacheName()}的所有缓存。")
    }

    abstract fun reload(key: String)

    abstract fun reloadAll(clear: Boolean)

    override fun afterPropertiesSet() {
        reloadAll(false)
    }

}