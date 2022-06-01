package io.kuark.service.user.provider.context

import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.user.common.user.vo.account.UserAccountCacheItem
import io.kuark.service.user.common.user.vo.account.UserType
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
open class RequestUrlAuthorityMetadataSource : FilterInvocationSecurityMetadataSource {

    @Autowired
    private lateinit var rbacRoleBiz : IRbacRoleBiz

    companion object {
        internal const val ROLE_SUPERUSER = "ROLE_SUPERUSER"
    }

    /**
     * 获取请求的URL对应需要的角色
     */
    override fun getAttributes(any: Any): Collection<ConfigAttribute> {
        val requestUrl = (any as FilterInvocation).requestUrl
        val subSysCode = KuarkContextHolder.get().subSysCode!!
        val user = KuarkContextHolder.get().user
        val roles = mutableListOf<String>()
        if (user != null) {
            // 处理主账号权限
            val userTypeDictCode = (user as UserAccountCacheItem).userTypeDictCode
            if (userTypeDictCode == UserType.MAIN_ACCOUNT.code) {
                roles.add(ROLE_SUPERUSER)
            }
        }
        val roleIds = rbacRoleBiz.getUrlAccessRoleIdsFromCache(subSysCode, requestUrl)
        roles.addAll(roleIds)
        val configs = roles.map { SecurityConfig(it) }
        return configs.ifEmpty {
            throw org.springframework.security.access.AccessDeniedException("权限不足")
        }
    }

    override fun getAllConfigAttributes(): Collection<ConfigAttribute>? {
        return null
    }

    override fun supports(clazz: Class<*>?): Boolean {
        return FilterInvocation::class.java.isAssignableFrom(clazz)
    }


}