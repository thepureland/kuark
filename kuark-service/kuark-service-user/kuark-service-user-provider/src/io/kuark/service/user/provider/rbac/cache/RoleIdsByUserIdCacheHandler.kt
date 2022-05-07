package io.kuark.service.user.provider.rbac.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.service.user.provider.rbac.dao.RbacRoleUserDao
import io.kuark.service.user.provider.rbac.model.po.RbacRoleUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class RoleIdsByUserIdCacheHandler : AbstractCacheHandler<List<String>>() {

    @Autowired
    private lateinit var rbacRoleUserDao: RbacRoleUserDao

    @Autowired
    private lateinit var self: RoleIdsByUserIdCacheHandler

    private val log = LogFactory.getLog(RoleIdsByUserIdCacheHandler::class)

    companion object {
        private const val CACHE_NAME = "rbac_role_ids_by_user_id"
    }


    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): List<String>? {
        return self.getRoleIdsByUserId(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("${CACHE_NAME}缓存未开启，不加载和缓存所有角色ids-用户id的关联数据！")
            return
        }

        // 加载所有角色ids-用户id的关联数据
        val results = rbacRoleUserDao.allSearch()
        log.debug("从数据库加载了${results.size}条角色id-用户id的关联数据。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存
        val roleIdMap = results.groupBy { it.userId }
        roleIdMap.forEach {
            val roleIds = it.value.map { roleUser -> roleUser.roleId }
            CacheKit.putIfAbsent(CACHE_NAME, it.key, roleIds)
        }
        log.debug("缓存了${roleIdMap.size}个用户共${results.size}条角色id的数据。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#userId",
        unless = "#result == null || #result.size() == 0"
    )
    open fun getRoleIdsByUserId(userId: String): List<String> {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("${CACHE_NAME}缓存中不存在key为${userId}的数据，从数据库中加载...")
        }
        val roleIds = rbacRoleUserDao.oneSearchProperty(RbacRoleUser::userId.name, userId, RbacRoleUser::roleId.name)
        log.debug("从数据库中加载userId为${userId}的roleId共${roleIds.size}条。")
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return roleIds as List<String>
    }

    open fun syncOnUpdate(userId: String, roleIds: Collection<String>) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("userId关联的角色更新后，同步${CACHE_NAME}缓存...")
            CacheKit.evict(CACHE_NAME, userId)
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                CacheKit.put(CACHE_NAME, userId, roleIds)
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    open fun syncOnDelete(userId: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("删除id为${userId}的用户后，同步从${CACHE_NAME}缓存中踢除...")
            CacheKit.evict(CACHE_NAME, userId)
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    open fun syncOnBatchDelete(userIds: Collection<String>) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("批量删除id为${userIds}的用户后，同步从${CACHE_NAME}缓存中踢除...")
            userIds.forEach {
                CacheKit.evict(CACHE_NAME, it)
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }
    
}