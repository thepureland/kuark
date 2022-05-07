package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.support.AbstractByIdCacheHandler
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.provider.dao.SysResourceDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class ResourceByIdCacheHandler : AbstractByIdCacheHandler<String, SysResourceCacheItem, SysResourceDao>() {

    @Autowired
    private lateinit var self: ResourceByIdCacheHandler
    
    companion object {
        private const val CACHE_NAME = "sys_resource_by_id"
    }

    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): SysResourceCacheItem? {
        return self.getResourceById(key)
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#id",
        unless = "#result == null"
    )
    open fun getResourceById(id: String): SysResourceCacheItem? {
        return getById(id)
    }

    @BatchCacheable(
        cacheNames = [CACHE_NAME],
        valueClass = SysResourceCacheItem::class
    )
    open fun getResourcesByIds(ids: Collection<String>): Map<String, SysResourceCacheItem> {
        return getByIds(ids)
    }

    override fun itemDesc() = "资源"

}