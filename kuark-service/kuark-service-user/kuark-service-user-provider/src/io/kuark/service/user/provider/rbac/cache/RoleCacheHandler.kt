package io.kuark.service.user.provider.rbac.cache

import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.base.query.Criterion
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.user.common.rbac.vo.role.RbacRoleCacheItem
import io.kuark.service.user.common.rbac.vo.role.RbacRoleSearchPayload
import io.kuark.service.user.provider.rbac.dao.RbacRoleDao
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class RoleCacheHandler: AbstractCacheHandler<RbacRoleCacheItem>() {

    @Autowired
    private lateinit var rbacRoleDao: RbacRoleDao

    companion object {
        private const val CACHE_NAME = "rbac_role_by_id"
        private val log = LogFactory.getLog(RoleCacheHandler::class)
    }


    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): RbacRoleCacheItem? = getSelf().getRoleFromCache(key)

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("缓存未开启，不加载和缓存所有启用状态的角色！")
            return
        }

        // 加载所有可用的角色
        val searchPayload = RbacRoleSearchPayload().apply {
            active = true
            returnEntityClass = RbacRoleCacheItem::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val roles = rbacRoleDao.search(searchPayload) as List<RbacRoleCacheItem>
        log.info("从数据库加载了${roles.size}条角色信息。")

        // 清除缓存
        if (clear && CacheKit.isCacheActive(CACHE_NAME)) {
            clear()
        }

        // 缓存角色
        roles.forEach {
            CacheKit.putIfAbsent(CACHE_NAME, it.id!!, it)
        }
        log.info("缓存了${roles.size}条角色信息。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#roleId",
        unless = "#result == null"
    )
    open fun getRoleFromCache(roleId: String): RbacRoleCacheItem? {
        require(roleId.isNotBlank()) { log.error("从缓存中获取角色时必须指定角色id！") }
        if (CacheKit.isCacheActive(CACHE_NAME)) log.debug("缓存中不存在id为${roleId}的角色，从数据库中加载...")
        var result = rbacRoleDao.get(roleId, RbacRoleCacheItem::class)
        if (result == null) {
            log.warn("数据库中不存在id为${roleId}的角色！")
        } else {
            log.debug("从数据库中加载id为${roleId}的角色成功。")
            if (result.active == true) {
                if (CacheKit.isCacheActive(CACHE_NAME)) log.debug("缓存从数据库加载的角色。")
            } else {
                log.debug("从数据库中加载id为${roleId}的角色为未启用状态，返回null，且不缓存。")
                result = null
            }
        }
        return result
    }

    @BatchCacheable(
        cacheNames = [CACHE_NAME],
        valueClass = RbacRoleCacheItem::class
    )
    open fun getRolesFromCache(roleIds: Collection<String>): Map<String, RbacRoleCacheItem> {
        require(roleIds.isNotEmpty()) { log.error("批量从缓存中获取角色时，角色id集合不能为空！") }

        // 加载所有可用的角色
        val searchPayload = RbacRoleSearchPayload().apply {
            active = true
            pageNo = null
            criterions = listOf(Criterion(RbacRole::id.name, Operator.IN, roleIds))
            returnEntityClass = RbacRoleCacheItem::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val roles = rbacRoleDao.search(searchPayload) as List<RbacRoleCacheItem>
        log.debug("从数据库加载了${roles.size}条角色信息。")

        return roles.associateBy { it.id!! }
    }

    fun syncOnInsert(id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("新增id为${id}的角色后，同步${CACHE_NAME}缓存...")
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                getSelf().getRoleFromCache(id)  // 缓存角色
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnUpdate(id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${id}的角色后，同步${CACHE_NAME}缓存...")
            CacheKit.evict(CACHE_NAME, id) // 踢除角色缓存
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                getSelf().getRoleFromCache(id) // 缓存角色
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnUpdateActive(id: String, active: Boolean) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${id}的角色的启用状态后，同步${CACHE_NAME}缓存...")
            if (active) {
                if (CacheKit.isWriteInTime(CACHE_NAME)) {
                    getSelf().getRoleFromCache(id) // 缓存角色
                }
            } else {
                CacheKit.evict(CACHE_NAME, id) // 踢除角色缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnDelete(id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("删除id为${id}的角色后，同步从${CACHE_NAME}缓存中踢除...")
            CacheKit.evict(CACHE_NAME, id) // 踢除缓存
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun synchOnBatchDelete(ids: Collection<String>) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("批量删除id为${ids}的角色后，同步从${CACHE_NAME}缓存中踢除...")
            ids.forEach {
                CacheKit.evict(CACHE_NAME, it) // 踢除角色缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun getSelf(): RoleCacheHandler {
        return SpringKit.getBean(RoleCacheHandler::class)
    }

}