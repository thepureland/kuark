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
class RoleCacheManagementSupport: AbstractCacheManagementSupport<RbacRoleDetail>() {

    @Autowired
    private lateinit var rbacRoleBiz: IRbacRoleBiz

    override fun cacheName(): String = RbacCacheNames.RBAC_ROLE

    override fun doReload(key: String): RbacRoleDetail? = rbacRoleBiz.getRoleFromCache(key)

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的角色！")
            return
        }

        // 加载所有可用的角色
        val searchPayload = RbacRoleSearchPayload().apply {
            active = true
            returnEntityClass = RbacRoleDetail::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val roles = rbacRoleBiz.search(searchPayload) as List<RbacRoleDetail>
        log.info("从数据库加载了${roles.size}条角色信息。")

        // 清除缓存
        if (clear && CacheKit.isCacheActive()) {
            clear()
        }

        // 缓存角色
        roles.forEach {
            CacheKit.putIfAbsent(cacheName(), it.id!!, it)
        }
        log.info("缓存了${roles.size}条角色信息。")
    }

}