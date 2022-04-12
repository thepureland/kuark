package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManager
import io.kuark.base.bean.BeanKit
import io.kuark.base.support.Consts
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.service.sys.common.vo.cache.SysCacheCacheItem
import io.kuark.service.sys.common.vo.cache.SysCacheSearchPayload
import io.kuark.service.sys.provider.dao.SysCacheDao
import io.kuark.service.sys.provider.model.po.SysCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class CacheConfigCacheManager : AbstractCacheManager<SysCacheCacheItem>() {

    @Autowired
    private lateinit var sysCacheDao: SysCacheDao

    @Autowired
    private lateinit var self: CacheConfigCacheManager

    companion object {
        private const val SYS_CACHE_BY_NAME = "sys_cache_by_name"
    }

    override fun cacheName(): String = SYS_CACHE_BY_NAME

    override fun doReload(key: String): SysCacheCacheItem? {
        return self.getCacheFromCache(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有缓存配置信息！")
            return
        }

        // 加载所有可用的缓存配置
        val searchPayload = ListSearchPayload().apply {
            returnEntityClass = SysCacheCacheItem::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val cacheConfigs = sysCacheDao.search(searchPayload) as List<SysCacheCacheItem>
        log.debug("从数据库加载了${cacheConfigs.size}条缓存配置信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存缓存配置
        cacheConfigs.forEach {
            CacheKit.putIfAbsent(cacheName(), it.name!!, it)
        }
        log.debug("缓存了${cacheConfigs.size}条缓存配置。")
    }

    @Cacheable(
        cacheNames = [SYS_CACHE_BY_NAME],
        key = "#name",
        unless = "#result == null"
    )
    open fun getCacheFromCache(name: String): SysCacheCacheItem? {
        if (CacheKit.isCacheActive()) {
            log.debug("缓存中不存在名称为${name}的缓存配置信息，从数据库中加载...")
        }
        val searchPayload = SysCacheSearchPayload().apply {
            returnEntityClass = SysCacheCacheItem::class
            this.name = name
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val result = sysCacheDao.search(searchPayload).firstOrNull() as SysCacheCacheItem?
        if (result == null) {
            log.warn("数据库中不存在名称为${name}的缓存配置信息！")
        } else {
            log.debug("数据库加载到名称为${name}的缓存配置信息.")
        }
        return result
    }

    fun syncOnInsert(any: Any, id: String) {
        if (CacheKit.isCacheActive()) {
            log.debug("新增id为${id}的缓存配置后，同步${cacheName()}缓存...")
            val name = BeanKit.getProperty(any, SysCache::name.name) as String
            self.getCacheFromCache(name) // 缓存
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnUpdate(any: Any, id: String) {
        if (CacheKit.isCacheActive()) {
            log.debug("更新id为${id}的缓存配置后，同步${cacheName()}缓存...")
            val name = BeanKit.getProperty(any, SysCache::name.name) as String
            CacheKit.evict(cacheName(), name) // 踢除缓存配置缓存
            self.getCacheFromCache(name) // 重新缓存
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnDelete(id: String, name: String) {
        if (CacheKit.isCacheActive()) {
            log.debug("删除id为${id}的缓存配置后，同步从${cacheName()}缓存中踢除...")
            CacheKit.evict(cacheName(), name) // 踢除缓存
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun synchOnBatchDelete(ids: Collection<String>, sysCaches: List<SysCache>) {
        if (CacheKit.isCacheActive()) {
            log.debug("批量删除id为${ids}的缓存配置后，同步从${cacheName()}缓存中踢除...")
            sysCaches.forEach {
                CacheKit.evict(cacheName(), it.name) // 踢除缓存
            }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

}