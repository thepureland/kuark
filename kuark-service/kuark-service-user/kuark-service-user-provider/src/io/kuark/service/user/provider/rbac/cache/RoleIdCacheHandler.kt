package io.kuark.service.user.provider.rbac.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.user.common.rbac.vo.role.RbacRoleCacheItem
import io.kuark.service.user.common.rbac.vo.role.RbacRoleRecord
import io.kuark.service.user.common.rbac.vo.role.RbacRoleSearchPayload
import io.kuark.service.user.provider.rbac.dao.RbacRoleDao
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class RoleIdCacheHandler : AbstractCacheHandler<List<String>>() {

    @Autowired
    private lateinit var rbacRoleDao: RbacRoleDao

    @Autowired
    private lateinit var roleCacheManager: RoleCacheHandler

    companion object {
        private const val CACHE_NAME = "rbac_role_id_by_sub_sys_and_tenant_id"
        private val log = LogFactory.getLog(RoleIdCacheHandler::class)
    }

    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): List<String> {
        require(key.contains(":")) { "缓存${CACHE_NAME}的key格式必须是 子系统代码::租户id" }
        val subSysAndTenantId = key.split(":")
        return getSelf().getRoleIdsFromCache(subSysAndTenantId[0], subSysAndTenantId[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("缓存未开启，不加载和缓存所有启用状态的角色id！")
            return
        }

        // 加载所有可用的角色id
        val searchPayload = RbacRoleSearchPayload().apply {
            active = true
            returnEntityClass = RbacRoleRecord::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val roles = rbacRoleDao.search(searchPayload) as List<RbacRoleRecord>
        log.info("从数据库加载了${roles.size}条角色信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存角色id
        val map = mutableMapOf<String, MutableList<String>>()
        roles.forEach {
            val key = "${it.subSysDictCode}::${it.tenantId}"
            var roleIds = map[key]
            if (roleIds == null) {
                roleIds = mutableListOf()
                map[key] = roleIds
            }
            roleIds.add(it.id!!)
        }
        map.forEach { (key, value) ->
            CacheKit.putIfAbsent(CACHE_NAME, key, value)
            log.debug("缓存了key为${key}的${value.size}条角色id。")
        }
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#subSysDictCode.concat('::').concat(#tenantId)",
        unless = "#result == null || #result.size() == 0"
    )
    open fun getRoleIdsFromCache(subSysDictCode: String, tenantId: String?): List<String> {
        require(subSysDictCode.isNotBlank()) { log.error("从缓存中获取角色id时，必须指定子系统代码！") }
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("缓存中不存在子系统为${subSysDictCode}且租户id为${tenantId}的角色id，从数据库中加载...")
        }
        val criteria = Criteria(RbacRole::subSysDictCode.name, Operator.EQ, subSysDictCode)
        criteria.addAnd(RbacRole::active.name, Operator.EQ, true)
        if (StringKit.isNotBlank(tenantId)) {
            criteria.addAnd(RbacRole::tenantId.name, Operator.EQ, tenantId)
        }
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val roleIds = rbacRoleDao.searchProperty(criteria, RbacRole::id.name) as List<String>
        log.debug("从数据库中加载到的角色id数量为${roleIds.size}。")
        return roleIds
    }

    fun syncOnInsert(id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("新增id为${id}的角色后，同步${CACHE_NAME}缓存...")
            val role = roleCacheManager.getRoleFromCache(id)!! // 缓存角色
            CacheKit.evict(CACHE_NAME, "${role.subSysDictCode}::${role.tenantId}") // 踢除角色id的缓存
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                getSelf().getRoleIdsFromCache(role.subSysDictCode!!, role.tenantId)  // 缓存角色id
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnUpdateActive(id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${id}的角色的启用状态后，同步${CACHE_NAME}缓存...")
            val r = roleCacheManager.getRoleFromCache(id)!!
            CacheKit.evict(CACHE_NAME, "${r.subSysDictCode}::${r.tenantId}") // 踢除角色id的缓存
            getSelf().getRoleIdsFromCache(r.subSysDictCode!!, r.tenantId)  // 缓存角色id
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnDelete(role: RbacRoleCacheItem) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("删除id为${role.id}的角色后，同步从${CACHE_NAME}缓存中踢除...")
            CacheKit.evict(CACHE_NAME, "${role.subSysDictCode}::${role.tenantId}") // 踢除角色id的缓存
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                getSelf().getRoleIdsFromCache(role.subSysDictCode!!, role.tenantId)  // 缓存角色id
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun synchOnBatchDelete(ids: Collection<String>, roleMap: Map<String, RbacRoleCacheItem>) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("批量删除id为${ids}的角色后，同步从${CACHE_NAME}缓存中踢除...")
            val keys = roleMap.map { "${it.value.subSysDictCode}::${it.value.tenantId}" }.toSet()
            keys.forEach {
                CacheKit.evict(CACHE_NAME, it) // 踢除角色id缓存
                if (CacheKit.isWriteInTime(CACHE_NAME)) {
                    val subSysAndTenantId = it.split(":")
                    getSelf().getRoleIdsFromCache(subSysAndTenantId[0], subSysAndTenantId[1])  // 缓存角色id
                }
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun getSelf(): RoleIdCacheHandler {
        return SpringKit.getBean(RoleIdCacheHandler::class)
    }

}