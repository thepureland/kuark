package io.kuark.cache.kit

import io.kuark.base.log.LogFactory
import io.kuark.cache.core.MixCacheManager
import io.kuark.context.spring.SpringKit
import org.springframework.cache.Cache
import kotlin.reflect.KClass

/**
 * 缓存工具类
 *
 * @author K
 * @since 1.0.0
 */
object CacheKit {

    private val log = LogFactory.getLog(this::class)

    /**
     * 根据名称获取缓存
     *
     * @param name 缓存名称
     * @return 缓存对象
     * @author K
     * @since 1.0.0
     */
    fun getCache(name: String): Cache? {
//        val cacheManager = SpringKit.getBean(MixCacheManager::class) //??? 在suspend方法中，会阻塞，原因不明
        val cacheManager = SpringKit.getBean("cacheManager") as MixCacheManager
        val cache = cacheManager.getCache(name)
        if (cache == null) {
            log.warn("缓存【$name】不存在！")
        }
        return cache
    }

    /**
     * 获取缓存中指定key的值
     *
     * @param cacheName 缓存名称
     * @param key 缓存key
     * @param valueClass 缓存key对应的值的类型
     * @return 缓存key对应的值
     * @author K
     * @since 1.0.0
     */
    fun <T : Any> getValue(cacheName: String, key: Any, valueClass: KClass<T>): T? {
        return getCache(cacheName)!!.get(key, valueClass.java)
    }

    /**
     * 获取缓存中指定key的值
     *
     * @param cacheName 缓存名称
     * @param key 缓存key
     * @return 缓存key对应的值
     * @author K
     * @since 1.0.0
     */
    fun getValue(cacheName: String, key: Any): Cache.ValueWrapper? {
        return getCache(cacheName)!!.get(key)
    }

    /**
     * 写入缓存
     *
     * @param cacheName 缓存名称
     * @param key 缓存key
     * @param value 要缓存的值
     * @author K
     * @since 1.0.0
     */
    fun put(cacheName: String, key: Any, value: Any?) {
        getCache(cacheName)!!.put(key, value)
    }

    /**
     * 如果不存在，就写入缓存
     *
     * @param cacheName 缓存名称
     * @param key 缓存key
     * @param value 要缓存的值
     * @author K
     * @since 1.0.0
     */
    fun putIfAbsent(cacheName: String, key: Any, value: Any?) {
        getCache(cacheName)!!.putIfAbsent(key, value)
    }

    /**
     * 踢除缓存
     *
     * @param cacheName 缓存名称
     * @param key 缓存key
     * @author K
     * @since 1.0.0
     */
    fun evict(cacheName: String, key: Any) {
        getCache(cacheName)!!.evict(key)
    }

}