package io.kuark.service.user.provider.rbac.cache

import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractByIdCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.context.kit.SpringKit
import io.kuark.service.user.common.rbac.vo.role.RbacRoleCacheItem
import io.kuark.service.user.provider.rbac.dao.RbacRoleDao
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class RoleByIdCacheHandler : AbstractByIdCacheHandler<String, RbacRoleCacheItem, RbacRoleDao>() {

    companion object {
        private const val CACHE_NAME = "rbac_role_by_id"
        private val log = LogFactory.getLog(RoleByIdCacheHandler::class)
    }


    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): RbacRoleCacheItem? = getSelf().getRoleById(key)

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#roleId",
        unless = "#result == null"
    )
    open fun getRoleById(roleId: String): RbacRoleCacheItem? {
        return getById(roleId)
    }

    @BatchCacheable(
        cacheNames = [CACHE_NAME],
        valueClass = RbacRoleCacheItem::class
    )
    open fun getRolesByIds(roleIds: Collection<String>): Map<String, RbacRoleCacheItem> {
        return getByIds(roleIds)
    }

    fun syncOnUpdateActive(id: String, active: Boolean) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${id}的角色的启用状态后，同步${CACHE_NAME}缓存...")
            if (active) {
                if (CacheKit.isWriteInTime(CACHE_NAME)) {
                    getSelf().getRoleById(id) // 缓存角色
                }
            } else {
                CacheKit.evict(CACHE_NAME, id) // 踢除角色缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun getSelf(): RoleByIdCacheHandler {
        return SpringKit.getBean(RoleByIdCacheHandler::class)
    }

    override fun itemDesc() = "角色"

}