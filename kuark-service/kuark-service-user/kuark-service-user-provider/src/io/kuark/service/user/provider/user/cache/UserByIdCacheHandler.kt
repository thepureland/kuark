package io.kuark.service.user.provider.user.cache

import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.support.AbstractByIdCacheHandler
import io.kuark.service.user.common.user.vo.account.UserAccountCacheItem
import io.kuark.service.user.provider.user.dao.UserAccountDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class UserByIdCacheHandler : AbstractByIdCacheHandler<String, UserAccountCacheItem, UserAccountDao>() {

    @Autowired
    private lateinit var self: UserByIdCacheHandler

    companion object {
        private const val CACHE_NAME = "user_by_id"
    }

    override fun cacheName() = CACHE_NAME

    override fun doReload(key: String): UserAccountCacheItem? {
        return self.getUserById(key)
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#id",
        unless = "#result == null"
    )
    open fun getUserById(id: String): UserAccountCacheItem? {
        return getById(id)
    }

    @BatchCacheable(
        cacheNames = [CACHE_NAME],
        valueClass = UserAccountCacheItem::class
    )
    open fun getUsersByIds(ids: Collection<String>): Map<String, UserAccountCacheItem> {
        return getByIds(ids)
    }

    override fun itemDesc() = "用户"

}