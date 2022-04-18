package io.kuark.service.user.provider.rbac.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.user.provider.rbac.dao.RbacRoleResourceDao
import io.kuark.service.user.provider.rbac.model.po.RbacRoleResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class ResourceIdsByRoleIdCacheHandler : AbstractCacheHandler<List<String>>() {

    @Autowired
    private lateinit var rbacRoleResourceDao: RbacRoleResourceDao

    companion object {
        private const val CACHE_NAME = "rbac_resource_ids_by_role_id"
        private val log = LogFactory.getLog(ResourceIdsByRoleIdCacheHandler::class)
    }

    override fun cacheName() = CACHE_NAME

    override fun doReload(key: String): List<String>? {
        return getSelf().getResourceIdsByRoleId(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("${CACHE_NAME}缓存未开启，不加载和缓存所有角色-资源id的关联数据！")
            return
        }

        // 加载所有角色-资源id的关联数据
        val results = rbacRoleResourceDao.allSearch()
        log.debug("从数据库加载了${results.size}条角色-资源id的关联数据。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存
        val userIdMap = results.groupBy { it.roleId }
        userIdMap.forEach {
            val values = it.value.map { roleRes -> roleRes.resourceId }
            CacheKit.putIfAbsent(CACHE_NAME, it.key, values)
        }
        log.debug("缓存了${userIdMap.size}个角色共${results.size}条资源id的数据。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#roleId",
        unless = "#result == null || #result.size() == 0"
    )
    open fun getResourceIdsByRoleId(roleId: String): List<String> {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("${CACHE_NAME}缓存中不存在key为${roleId}的数据，从数据库中加载...")
        }
        val resIds = rbacRoleResourceDao.oneSearchProperty(
            RbacRoleResource::roleId.name, roleId, RbacRoleResource::resourceId.name
        )
        log.debug("从数据库中加载roleId为${roleId}的resourceId共${resIds.size}条。")
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return resIds as List<String>
    }

    fun syncOnUpdate(roleId: String, resourceIds: Collection<String>) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("roleId关联的资源更新后，同步${CACHE_NAME}缓存...")
            CacheKit.evict(CACHE_NAME, roleId)
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                CacheKit.put(CACHE_NAME, roleId, resourceIds)
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

    fun syncOnBatchDelete(ids: Collection<String>) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("批量删除id为${ids}的角色后，同步从${CACHE_NAME}缓存中踢除...")
            ids.forEach {
                CacheKit.evict(CACHE_NAME, it) // 踢除角色缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    private fun getSelf(): ResourceIdsByRoleIdCacheHandler {
        return SpringKit.getBean(ResourceIdsByRoleIdCacheHandler::class)
    }

}