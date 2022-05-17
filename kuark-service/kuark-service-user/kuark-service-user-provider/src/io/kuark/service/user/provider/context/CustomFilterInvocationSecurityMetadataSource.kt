package io.kuark.service.user.provider.context

import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.access.SecurityConfig
import org.springframework.security.web.FilterInvocation
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource
import org.springframework.stereotype.Component


/**
 * 从当前请求url中判断属于哪些权限
 */
@Component
open class CustomFilterInvocationSecurityMetadataSource : FilterInvocationSecurityMetadataSource {

    @Autowired
    private lateinit var rbacRoleBiz : IRbacRoleBiz

    override fun getAttributes(any: Any): Collection<ConfigAttribute> {
        val requestUrl = (any as FilterInvocation).requestUrl
        val subSysCode = KuarkContextHolder.get().subSysCode!!
        val roleIds = rbacRoleBiz.getUrlAccessRoleIdsFromCache(subSysCode, requestUrl)
        val configs = roleIds.map { SecurityConfig(it) }
        return configs.ifEmpty {
            SecurityConfig.createList("ROLE_LOGIN")
        }
    }

    override fun getAllConfigAttributes(): Collection<ConfigAttribute>? {
        return null
    }

    override fun supports(clazz: Class<*>?): Boolean {
        return FilterInvocation::class.java.isAssignableFrom(clazz)
    }


}