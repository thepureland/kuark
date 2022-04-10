package io.kuark.service.user.provider.rbac.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManagementSupport
import io.kuark.base.support.Consts
import io.kuark.service.user.common.rbac.vo.role.RbacRoleDetail
import io.kuark.service.user.common.rbac.vo.role.RbacRoleSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class RoleIdCacheManagementSupport: AbstractCacheManagementSupport<List<String>>() {

    @Autowired
    private lateinit var rbacRoleBiz: IRbacRoleBiz

    override fun cacheName(): String = RbacCacheNames.RBAC_ROLE_ID

    override fun doReload(key: String): List<String> {
        require(key.contains(":")) { "缓存${cacheName()}的key格式必须是 子系统代码:租户id" }
        val subSysAndTenantId = key.split(":")
        return rbacRoleBiz.getRoleIdsFromCache(subSysAndTenantId[0], subSysAndTenantId[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的角色id！")
            return
        }

        // 加载所有可用的角色id
        val searchPayload = RbacRoleSearchPayload().apply {
            active = true
            returnEntityClass = RbacRoleDetail::class
        }
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val roles = rbacRoleBiz.search(searchPayload) as List<RbacRoleDetail>
        log.info("从数据库加载了${roles.size}条角色信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存角色id
        val map = mutableMapOf<String, MutableList<String>>()
        roles.forEach {
            val key = "${it.subSysDictCode}:${it.tenantId}"
            var roleIds = map[key]
            if (roleIds == null) {
                roleIds = mutableListOf()
                map[key] = roleIds
            }
            roleIds.add(it.id!!)
        }
        map.forEach { (key, value) ->
            CacheKit.putIfAbsent(cacheName(), key, value)
            log.debug("缓存了key为${key}的${value.size}条角色id。")
        }
    }

}