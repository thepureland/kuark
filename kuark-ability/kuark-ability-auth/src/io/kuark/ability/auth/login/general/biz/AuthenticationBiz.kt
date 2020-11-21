package io.kuark.ability.auth.login.general.biz

import io.kuark.ability.auth.context.PrincipalToken
import io.kuark.ability.auth.login.general.PrincipalType
import io.kuark.ability.auth.login.general.ibiz.IAuthenticationBiz
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.env.DefaultEnvironment


open class AuthenticationBiz : IAuthenticationBiz {


    override fun login(map: Map<String, Any>) {
        val env = DefaultEnvironment()
        SecurityUtils.setSecurityManager(env.securityManager)
        val principalsTypeCode = map["principalsTypeCode"] as String
        val principalsType = PrincipalType.valueOf(principalsTypeCode)
        val principalsIdentifier = map["principalsIdentifier"] as String
        val credentials = map["credentials"] as String
        var token: AuthenticationToken
//        when(principalsType) {
//            PrincipalsType.SYS_ACCOUT -> {
//                token = UsernamePasswordToken(principalsIdentifier, credentials)
//            }
//        }
        token = PrincipalToken(principalsIdentifier, credentials, principalsType)
        val subject = SecurityUtils.getSubject()
        subject.login(token)


        map["credentials"]


    }

    override fun logout() {
        TODO("Not yet implemented")
    }

}