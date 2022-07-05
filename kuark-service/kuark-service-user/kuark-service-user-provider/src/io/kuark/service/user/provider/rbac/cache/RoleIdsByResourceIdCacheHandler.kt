package io.kuark.service.user.provider.rbac.cache

import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.service.user.provider.rbac.dao.RbacRoleResourceDao
import io.kuark.service.user.provider.rbac.model.po.RbacRoleResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class RoleIdsByResourceIdCacheHandler : AbstractCacheHandler<Collection<String>>() {


    @Autowired
    private lateinit var rbacRoleResourceDao: RbacRoleResourceDao

    @Autowired
    private lateinit var self: RoleIdsByResourceIdCacheHandler

    private val log = LogFactory.getLog(RoleIdsByResourceIdCacheHandler::class)

    companion object {
        private const val CACHE_NAME = "rbac_role_ids_by_resource_id"
    }


    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): Collection<String>? {
        return self.getRoleIdsByResourceId(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("${CACHE_NAME}缓存未开启，不加载和缓存所有角色ids-资源id的关联数据！")
            return
        }

        // 加载所有角色ids-资源id的关联数据
        val results = rbacRoleResourceDao.allSearch()
        log.debug("从数据库加载了${results.size}条角色id-资源id的关联数据。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存
        val roleIdMap = results.groupBy { it.resourceId }
        roleIdMap.forEach {
            val roleIds = it.value.map { roleRes -> roleRes.roleId }
            CacheKit.putIfAbsent(CACHE_NAME, it.key, roleIds)
        }
        log.debug("缓存了${roleIdMap.size}个资源共${results.size}条角色id的数据。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#resourceId",
        unless = "#result == null || #result.size() == 0"
    )
    open fun getRoleIdsByResourceId(resourceId: String): Collection<String> {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("${CACHE_NAME}缓存中不存在key为${resourceId}的数据，从数据库中加载...")
        }
        val roleIds = rbacRoleResourceDao.oneSearchProperty(
            RbacRoleResource::resourceId.name, resourceId, RbacRoleResource::roleId.name
        )
        log.debug("从数据库中加载resourceId为${resourceId}的roleId共${roleIds.size}条。")
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return roleIds as List<String>
    }

    @BatchCacheable(
        cacheNames = [CACHE_NAME],
        valueClass = Collection::class
    )
    open fun getRoleIdsByResourceIds(resourceIds: Collection<String>): Map<String, Collection<String>> {
        val returnProperties = listOf(RbacRoleResource::roleId.name, RbacRoleResource::resourceId.name)
        val results = rbacRoleResourceDao.inSearchProperties(
            RbacRoleResource::resourceId.name, resourceIds, returnProperties
        )
        log.debug("从数据库中加载resourceIds为${resourceIds}的roleId共${results.size}条。")
        val result = mutableMapOf<String, MutableList<String>>()
        results.forEach { map ->
            val resourceId = map[RbacRoleResource::resourceId.name] as String
            val roleId = map[RbacRoleResource::roleId.name] as String
            val roleIds = result[resourceId] ?: mutableListOf()
            roleIds.add(roleId)
            result[resourceId] = roleIds
        }
        return result
    }

    open fun syncOnUpdate(resourceId: String, roleIds: Collection<String>) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("resourceId关联的角色更新后，同步${CACHE_NAME}缓存...")
            CacheKit.evict(CACHE_NAME, resourceId)
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                CacheKit.put(CACHE_NAME, resourceId, roleIds)
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    open fun syncOnDelete(resourceId: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("删除id为${resourceId}的资源后，同步从${CACHE_NAME}缓存中踢除...")
            CacheKit.evict(CACHE_NAME, resourceId)
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    open fun syncOnBatchDelete(resourceIds: Collection<String>) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("批量删除id为${resourceIds}的资源后，同步从${CACHE_NAME}缓存中踢除...")
            resourceIds.forEach {
                CacheKit.evict(CACHE_NAME, it)
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

}