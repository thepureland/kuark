package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.support.AbstractByIdCacheHandler
import io.kuark.context.kit.SpringKit
import io.kuark.service.sys.common.vo.dict.SysDictCacheItem
import io.kuark.service.sys.provider.dao.SysDictDao
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class DictByIdCacheHandler : AbstractByIdCacheHandler<String, SysDictCacheItem, SysDictDao>() {

    companion object {
        private const val CACHE_NAME = "sys_dict_by_id"
    }

    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): SysDictCacheItem? {
        return getSelf().getDictById(key)
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#dictId",
        unless = "#result == null"
    )
    open fun getDictById(dictId: String): SysDictCacheItem? {
        return getById(dictId)
    }

    @BatchCacheable(
        cacheNames = [CACHE_NAME],
        valueClass = SysDictCacheItem::class
    )
    open fun getDictsByIds(ids: Collection<String>): Map<String, SysDictCacheItem> {
        return getByIds(ids)
    }

    fun getSelf(): DictByIdCacheHandler {
        return SpringKit.getBean(DictByIdCacheHandler::class)
    }

    override fun itemDesc() = "字典"

}