package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.cache.SysCacheCacheItem
import io.kuark.service.sys.common.vo.cache.SysCacheSearchPayload
import io.kuark.service.sys.provider.dao.SysCacheDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
open class SysCacheManager : ICacheConfigCacheManager {

    protected val log = LogFactory.getLog(this::class)

    @Autowired
    private lateinit var sysCacheDao: SysCacheDao

    companion object {
        private const val SYS_CACHE_BY_NAME = "sys_cache_by_name"
    }

    @Cacheable(
        cacheNames = [SYS_CACHE_BY_NAME],
        key = "#name",
        unless = "#result == null"
    )
    override fun getCacheFromCache(name: String): SysCacheCacheItem? {
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

}