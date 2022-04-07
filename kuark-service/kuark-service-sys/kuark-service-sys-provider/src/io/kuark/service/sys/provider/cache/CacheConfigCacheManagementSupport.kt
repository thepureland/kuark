package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManagementSupport
import io.kuark.base.support.Consts
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.service.sys.common.vo.cache.SysCacheDetail
import io.kuark.service.sys.provider.biz.ibiz.ISysCacheBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class CacheConfigCacheManagementSupport: AbstractCacheManagementSupport<SysCacheDetail>() {

    @Autowired
    private lateinit var sysCacheBiz: ISysCacheBiz

    override fun cacheName(): String = SysCacheNames.SYS_CACHE

    override fun doReload(key: String): SysCacheDetail? {
        return sysCacheBiz.getCacheFromCache(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有缓存配置信息！")
            return
        }

        // 加载所有可用的缓存配置
        val searchPayload = ListSearchPayload().apply {
            returnEntityClass = SysCacheDetail::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val cacheConfigs = sysCacheBiz.search(searchPayload) as List<SysCacheDetail>
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

}