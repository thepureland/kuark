package io.kuark.service.user.provider.rbac.biz.impl

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManagementSupport
import io.kuark.ability.cache.support.CacheNames
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.service.user.common.rbac.vo.role.RbacRoleDetail
import io.kuark.service.user.common.rbac.vo.role.RbacRoleSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class RoleCacheManagementSupport: AbstractCacheManagementSupport<RbacRoleDetail>() {

    @Autowired
    private lateinit var rbacRoleBiz: IRbacRoleBiz

    override fun cacheName(): String {
        return CacheNames.RBAC_ROLE
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun keys(): Set<String> {
        val criteria = Criteria(RbacRole::active.name, Operator.EQ, true)
        val ids = rbacRoleBiz.searchProperty(criteria, RbacRole::id.name) as List<String>
        return ids.filter { isExists(it) }.toSet()
    }

    override fun values(): List<RbacRoleDetail> {
        TODO("Not yet implemented")
    }

    override fun reload(key: String) {
        evict(key)
        log.info("手动重载名称为${cacheName()}，key为${key}的缓存...")
        val role = rbacRoleBiz.getRoleFromCache(key)
        if (role == null) {
            log.info("数据库中已不存在对应数据！")
        } else {
            log.info("重载成功。")
        }
    }

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
            CacheKit.putIfAbsent(CacheNames.RBAC_ROLE, it.id!!, it)
        }
        log.info("缓存了${roles.size}条角色信息。")
    }


}