package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.sys.common.vo.dict.SysTenantCacheItem
import io.kuark.service.sys.common.vo.tenant.SysTenantSearchPayload
import io.kuark.service.sys.provider.dao.SysTenantDao
import io.kuark.service.sys.provider.model.po.SysTenant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class TenantsBySubSysCacheHandler: AbstractCacheHandler<List<SysTenantCacheItem>>() {

    @Autowired
    private lateinit var dao: SysTenantDao

    companion object {
        private const val CACHE_NAME = "sys_tenants_by_sub_sys"
        private val log = LogFactory.getLog(TenantsBySubSysCacheHandler::class)
    }

    @Autowired
    private lateinit var tenantByIdCacheManager : TenantByIdCacheHandler


    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): List<SysTenantCacheItem> = getSelf().getTenantsFromCache(key)

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("缓存未开启，不加载和缓存所有启用状态的租户！")
            return
        }

        // 加载所有可用的租户
        val searchPayload = SysTenantSearchPayload().apply {
            returnEntityClass = SysTenantCacheItem::class
            active = true
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val tenants = dao.search(searchPayload) as List<SysTenantCacheItem>
        log.debug("从数据库加载了${tenants.size}条租户信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存租户
        val tenantMap = tenants.groupBy { it.subSysDictCode!! }
        tenantMap.forEach { (key, value) ->
            CacheKit.putIfAbsent(CACHE_NAME, key, value)
            log.debug("缓存了子系统${key}的${tenants.size}条租户信息。")
        }
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#subSysDictCode",
        unless = "#result == null || #result.isEmpty()"
    )
    open fun getTenantsFromCache(subSysDictCode: String): List<SysTenantCacheItem> {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("缓存中不存在子系统为${subSysDictCode}的租户，从数据库中加载...")
        }
        val searchPayload = SysTenantSearchPayload().apply {
            returnEntityClass = SysTenantCacheItem::class
            active = true
            this.subSysDictCode = subSysDictCode
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val tenants = dao.search(searchPayload) as List<SysTenantCacheItem>
        log.debug("从数据库加载了子系统为${subSysDictCode}的${tenants.size}条租户信息。")
        return tenants
    }

    fun syncOnInsert(any: Any, id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME) && CacheKit.isWriteInTime(CACHE_NAME)) {
            log.debug("新增id为${id}的租户后，同步${CACHE_NAME}缓存...")
            val subSysDictCode = BeanKit.getProperty(any, SysTenant::subSysDictCode.name) as String
            CacheKit.evict(CACHE_NAME, subSysDictCode) // 踢除缓存，因为缓存的粒度为子系统
            getSelf().getTenantsFromCache(subSysDictCode) // 重新缓存
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnUpdate(any: Any?, id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${id}的租户后，同步${CACHE_NAME}缓存...")
            val subSysDictCode = if (any == null) {
                tenantByIdCacheManager.getTenantFromCache(id)!!.subSysDictCode!!
            } else {
                BeanKit.getProperty(any, SysTenant::subSysDictCode.name) as String
            }
            CacheKit.evict(CACHE_NAME, subSysDictCode) // 踢除缓存，因为缓存的粒度为子系统
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                getSelf().getTenantsFromCache(subSysDictCode) // 重新缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnDelete(sysTenant: SysTenantCacheItem) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("删除id为${sysTenant.id}的租户后，同步从${CACHE_NAME}缓存中踢除...")
            val subSysDictCode = sysTenant.subSysDictCode!!
            CacheKit.evict(CACHE_NAME, subSysDictCode) // 踢除缓存，缓存的粒度为子系统
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                getSelf().getTenantsFromCache(subSysDictCode) // 重新缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun synchOnBatchDelete(ids: Collection<String>, subSysDictCodes: Set<String>) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("批量删除id为${ids}的租户后，同步从${CACHE_NAME}缓存中踢除...")
            subSysDictCodes.forEach {
                CacheKit.evict(CACHE_NAME, it) // 踢除缓存，缓存的粒度为子系统
                if (CacheKit.isWriteInTime(CACHE_NAME)) {
                    getSelf().getTenantsFromCache(it) // 重新缓存
                }
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun getSelf(): TenantsBySubSysCacheHandler {
        return SpringKit.getBean(TenantsBySubSysCacheHandler::class)
    }

}