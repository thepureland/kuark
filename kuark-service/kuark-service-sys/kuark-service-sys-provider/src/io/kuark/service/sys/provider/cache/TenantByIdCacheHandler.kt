package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.sys.common.vo.dict.SysTenantCacheItem
import io.kuark.service.sys.common.vo.tenant.SysTenantSearchPayload
import io.kuark.service.sys.provider.dao.SysTenantDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class TenantByIdCacheHandler : AbstractCacheHandler<SysTenantCacheItem>() {

    @Autowired
    private lateinit var dao: SysTenantDao

    companion object {
        private const val SYS_TENANT_BY_ID = "sys_tenant_by_id"
        private val log = LogFactory.getLog(TenantByIdCacheHandler::class)
    }

    override fun cacheName(): String = SYS_TENANT_BY_ID

    override fun doReload(key: String): SysTenantCacheItem? {
        return getSelf().getTenantFromCache(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(cacheName())) {
            log.info("缓存未开启，不加载和缓存所有租户！")
            return
        }

        // 加载所有可用的租户
        val searchPayload = SysTenantSearchPayload().apply {
            returnEntityClass = SysTenantCacheItem::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val tenants = dao.search(searchPayload) as List<SysTenantCacheItem>
        log.debug("从数据库加载了${tenants.size}条租户信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存租户
        tenants.forEach {
            CacheKit.putIfAbsent(cacheName(), it.id!!, it)
        }
        log.debug("缓存了${tenants.size}条租户信息。")
    }

    @Cacheable(
        cacheNames = [SYS_TENANT_BY_ID],
        key = "#id",
        unless = "#result == null"
    )
    open fun getTenantFromCache(id: String): SysTenantCacheItem? {
        if (CacheKit.isCacheActive(cacheName())) {
            log.debug("缓存中不存在id为${id}的租户，从数据库中加载...")
        }
        val result = dao.get(id, SysTenantCacheItem::class)
        if (result == null) {
            log.warn("数据库中不存在id为${id}的租户！")
        } else {
            log.debug("数据库加载到id为${id}的租户#${result.id}.")
        }
        return result
    }

    fun syncOnInsert(id: String) {
        if (CacheKit.isCacheActive(cacheName()) && CacheKit.isWriteInTime(cacheName())) {
            if (CacheKit.isWriteInTime(cacheName())) {
                log.debug("新增id为${id}的租户后，同步${cacheName()}缓存...")
                getSelf().getTenantFromCache(id)
                log.debug("${cacheName()}缓存同步完成。")
            }
        }
    }

    fun syncOnUpdate(id: String) {
        if (CacheKit.isCacheActive(cacheName())) {
            log.debug("更新id为${id}的租户后，同步${cacheName()}缓存...")
            CacheKit.evict(cacheName(), id)
            if (CacheKit.isWriteInTime(cacheName())) {
                getSelf().getTenantFromCache(id)
            }
            log.debug("${cacheName()}缓存同步完成.")
        }
    }

    fun syncOnDelete(id: String) {
        if (CacheKit.isCacheActive(cacheName())) {
            log.debug("删除id为${id}的租户后，同步从${cacheName()}缓存中踢除...")
            CacheKit.evict(cacheName(), id)
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun synchOnBatchDelete(ids: Collection<String>) {
        if (CacheKit.isCacheActive(cacheName())) {
            log.debug("批量删除id为${ids}的租户后，同步从${cacheName()}缓存中踢除...")
            ids.forEach { CacheKit.evict(cacheName(), it) }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun getSelf(): TenantByIdCacheHandler {
        return SpringKit.getBean(TenantByIdCacheHandler::class)
    }

}