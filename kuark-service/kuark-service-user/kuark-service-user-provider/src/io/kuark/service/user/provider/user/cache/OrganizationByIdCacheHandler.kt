package io.kuark.service.user.provider.user.cache

import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.base.query.Criterion
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.service.user.common.user.vo.organization.UserOrganizationCacheItem
import io.kuark.service.user.provider.user.dao.UserOrganizationDao
import io.kuark.service.user.provider.user.model.po.UserOrganization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
open class OrganizationByIdCacheHandler : AbstractCacheHandler<UserOrganizationCacheItem>() {

    @Autowired
    private lateinit var userOrganizationDao: UserOrganizationDao

    @Autowired
    private lateinit var self: OrganizationByIdCacheHandler

    private val log = LogFactory.getLog(OrganizationByIdCacheHandler::class)
    
    companion object {
        private const val CACHE_NAME = "user_organization_by_id"
    }


    override fun cacheName() = CACHE_NAME

    override fun doReload(key: String): UserOrganizationCacheItem? {
        return self.getOrganizationById(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("${CACHE_NAME}缓存未开启，不加载和缓存所有组织机构数据！")
            return
        }

        // 加载所有组织机构数据
        val results = userOrganizationDao.allSearch()
        log.debug("从数据库加载了${results.size}条组织机构数据。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存
        results.forEach {
            CacheKit.putIfAbsent(CACHE_NAME, it.id!!, it)
        }
        log.debug("缓存了${results.size}条组织机构数据。")
    }
    
    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#id",
        unless = "#result == null"
    )
    open fun getOrganizationById(id: String): UserOrganizationCacheItem? {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("${CACHE_NAME}缓存中不存在key为${id}的组织机构，从数据库中加载...")
        }
        val result = userOrganizationDao.get(id, UserOrganizationCacheItem::class)
        if (result == null) {
            log.warn("数据库中找不到id为${id}的组织机构！")
        } else {
            log.debug("从数据库中加载到id为${id}的组织机构。")
        }
        return result
    }

    @BatchCacheable(
        cacheNames = [CACHE_NAME],
        valueClass = UserOrganizationCacheItem::class
    )
    open fun getOrganizationsByIds(ids: Collection<String>): Map<String, UserOrganizationCacheItem> {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("${CACHE_NAME}缓存中没有找到所有这些id为${ids}的组织机构，从数据库中加载...")
        }
        val searchPayload = ListSearchPayload().apply {
            returnEntityClass = UserOrganizationCacheItem::class
            criterions = listOf(Criterion(UserOrganization::id.name, Operator.IN, ids))
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val results = userOrganizationDao.search(searchPayload) as List<UserOrganizationCacheItem>
        log.debug("数据库中加载到${results.size}条组织机构.")
        return results.associateBy { it.id!! }
    }

}