package io.kuark.service.user.provider.user.cache

import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.base.query.Criterion
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.user.common.user.vo.account.UserAccountCacheItem
import io.kuark.service.user.common.user.vo.account.UserAccountRecord
import io.kuark.service.user.common.user.vo.account.UserAccountSearchPayload
import io.kuark.service.user.provider.rbac.cache.ResourceIdsByRoleIdCacheHandler
import io.kuark.service.user.provider.user.dao.UserAccountDao
import io.kuark.service.user.provider.user.model.po.UserAccount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class UserByIdCacheHandler : AbstractCacheHandler<UserAccountCacheItem>() {

    @Autowired
    private lateinit var userAccountDao: UserAccountDao

    companion object {
        private const val CACHE_NAME = "user_by_id"
        private val log = LogFactory.getLog(UserByIdCacheHandler::class)
    }

    override fun cacheName() = CACHE_NAME

    override fun doReload(key: String): UserAccountCacheItem? {
        return getSelf().getUserById(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("${CACHE_NAME}缓存未开启，不加载和缓存所有用户账号数据！")
            return
        }

        // 加载所有用户账号数据
        val results = userAccountDao.allSearch()
        log.debug("从数据库加载了${results.size}条用户账号数据。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存
        results.forEach {
            CacheKit.putIfAbsent(CACHE_NAME, it.id!!, it)
        }
        log.debug("缓存了${results.size}条用户账号数据。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#id",
        unless = "#result == null"
    )
    open fun getUserById(id: String): UserAccountCacheItem? {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("${CACHE_NAME}缓存中不存在key为${id}的用户账号，从数据库中加载...")
        }
        val result = userAccountDao.get(id, UserAccountCacheItem::class)
        if (result == null) {
            log.warn("数据库中找不到id为${id}的用户账号！")
        } else {
            log.debug("从数据库中加载到id为${id}的用户账号。")
        }
        return result
    }

    @BatchCacheable(
        cacheNames = [CACHE_NAME],
        valueClass = UserAccountCacheItem::class
    )
    open fun getUsersByIds(ids: Collection<String>): List<UserAccountCacheItem> {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("${CACHE_NAME}缓存中没有找到所有这些id为${ids}的用户账号，从数据库中加载...")
        }
        val searchPayload = UserAccountSearchPayload().apply {
            returnEntityClass = UserAccountCacheItem::class
            criterions = listOf(Criterion(UserAccount::id.name, Operator.IN, ids))
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val results = userAccountDao.search(searchPayload) as List<UserAccountCacheItem>
        log.debug("数据库中加载到${results.size}条用户账号.")
        return results
    }

    private fun getSelf(): UserByIdCacheHandler {
        return SpringKit.getBean(UserByIdCacheHandler::class)
    }

}