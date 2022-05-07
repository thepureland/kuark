package io.kuark.service.user.provider.context

import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component


@Component
open class CustomAccessDecisionManager : AccessDecisionManager {

    override fun decide(authentication: Authentication, any: Any?, configAttributes: Collection<ConfigAttribute>) {
        for (configAttribute in configAttributes) {
            // 如果请求Url需要的角色是ROLE_LOGIN，说明当前的Url用户登录后即可访问
            if ("ROLE_LOGIN" == configAttribute.attribute && authentication is UsernamePasswordAuthenticationToken) {
                return
            }
            val auths = authentication.authorities //获取登录用户具有的角色
            for (grantedAuthority in auths) {
                if (configAttribute.attribute == grantedAuthority.authority) {
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