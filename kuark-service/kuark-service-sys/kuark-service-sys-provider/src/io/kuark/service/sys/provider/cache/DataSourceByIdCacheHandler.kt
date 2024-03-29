package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.support.AbstractByIdCacheHandler
import io.kuark.service.sys.common.vo.datasource.SysDataSourceCacheItem
import io.kuark.service.sys.provider.dao.SysDataSourceDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class DataSourceByIdCacheHandler: AbstractByIdCacheHandler<String, SysDataSourceCacheItem, SysDataSourceDao>() {

    @Autowired
    private lateinit var self: DataSourceByIdCacheHandler

    companion object {
        private const val CACHE_NAME = "sys_data_source_by_id"
    }

    override fun itemDesc() = "数据源"

    override fun cacheName() = CACHE_NAME

    override fun doReload(key: String): SysDataSourceCacheItem? {
        return self.getTenantById(key)
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#id",
        unless = "#result == null"
    )
    open fun getTenantById(id: String): SysDataSourceCacheItem? {
        return getById(id)
    }

    @BatchCacheable(
        cacheNames = [CACHE_NAME],
        valueClass = SysDataSourceCacheItem::class
    )
    open fun getTenantsByIds(ids: Collection<String>): Map<String, SysDataSourceCacheItem> {
        return getByIds(ids)
    }

}