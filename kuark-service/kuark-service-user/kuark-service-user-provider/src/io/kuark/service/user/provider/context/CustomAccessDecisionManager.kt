package io.kuark.service.user.provider.context

import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component


/**
 * 判断是否拥有权限
 */
@Component
open class CustomAccessDecisionManager : AccessDecisionManager {

    override fun decide(authentication: Authentication, any: Any?, configAttributes: Collection<ConfigAttribute>) {
        for (configAttribute in configAttributes) {
            val needRole = configAttribute.attribute
            // 如果请求Url需要的角色是ROLE_LOGIN，说明当前的Url用户登录后即可访问
            if ("ROLE_LOGIN" == needRole) {
                if (authentication is AnonymousAuthenticationToken) {
                    throw BadCredentialsException("unlogin");
                } else
                    return;
            }

            //当前用户所具有的权限
            val auths = authentication.authorities
            for (grantedAuthority in auths) {
                if ("ROLE_${configAttribute.attribute}" == grantedAuthority.authority) {
                    return
                }
            }
        }
        throw org.springframework.security.access.AccessDeniedException("权限不足")
    }

    override fun supports(attribute: ConfigAttribute?): Boolean {
        return true
    }

    override fun supports(clazz: Class<*>?): Boolean {
        return true
    }

}