package io.kuark.ability.auth.login.general.biz

import io.kuark.ability.auth.login.general.PrincipalsType
import io.kuark.ability.auth.login.general.ibiz.IAuthenticationBiz
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.env.DefaultEnvironment


open class AuthenticationBiz : IAuthenticationBiz {


    override fun login(map: Map<String, Any>) {
        val env = DefaultEnvironment(map)
        SecurityUtils.setSecurityManager(env.securityManager)
        val principalsTypeCode = map["principalsTypeCode"] as String
        val principalsType = PrincipalsType.valueOf(principalsTypeCode)
        val principalsIdentifier = map["principalsIdentifier"] as String
        val credentials = map["credentials"] as String
        var token: AuthenticationToken
//        when(principalsType) {
//            PrincipalsType.SYS_ACCOUT -> {
//                token = UsernamePasswordToken(principalsIdentifier, credentials)
//            }
//        }
        token = UsernamePasswordToken(principalsIdentifier, credentials)
        val subject = SecurityUtils.getSubject()
        subject.login(token)


        map["credentials"]


    }

    override fun logout() {
        TODO("Not yet implemented")
    }

}