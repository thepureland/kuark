package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.support.AbstractByIdCacheHandler
import io.kuark.service.sys.common.vo.dict.SysTenantCacheItem
import io.kuark.service.sys.provider.dao.SysTenantDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class TenantByIdCacheHandler : AbstractByIdCacheHandler<String, SysTenantCacheItem, SysTenantDao>() {

    @Autowired
    private lateinit var self: TenantByIdCacheHandler
    
    companion object {
        private const val CACHE_NAME = "sys_tenant_by_id"
    }

    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): SysTenantCacheItem? {
        return self.getTenantById(key)
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#id",
        unless = "#result == null"
    )
    open fun getTenantById(id: String): SysTenantCacheItem? {
        return getById(id)
    }

    @BatchCacheable(
        cacheNames = [CACHE_NAME],
        valueClass = SysTenantCacheItem::class
    )
    open fun getTenantsByIds(ids: Collection<String>): Map<String, SysTenantCacheItem> {
        return getByIds(ids)
    }

    override fun itemDesc() = "租户"

}