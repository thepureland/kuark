package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManagementSupport
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.tenant.SysTenantDetail
import io.kuark.service.sys.common.vo.tenant.SysTenantSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysTenantBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class TenantCacheManagementSupport: AbstractCacheManagementSupport<List<SysTenantDetail>>() {

    @Autowired
    private lateinit var sysTenantBiz: ISysTenantBiz

    override fun cacheName(): String = SysCacheNames.SYS_TENANT

    override fun doReload(key: String): List<SysTenantDetail> = sysTenantBiz.getTenantsFromCache(key)

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的租户！")
            return
        }

        // 加载所有可用的租户
        val searchPayload = SysTenantSearchPayload().apply {
            returnEntityClass = SysTenantDetail::class
            active = true
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val tenants = sysTenantBiz.search(searchPayload) as List<SysTenantDetail>
        log.debug("从数据库加载了${tenants.size}条租户信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存租户
        val tenantMap = tenants.groupBy { it.subSysDictCode!! }
        tenantMap.forEach { (key, value) ->
            CacheKit.putIfAbsent(cacheName(), key, value)
            log.debug("缓存了子系统${key}的${tenants.size}条租户信息。")
        }
    }

}