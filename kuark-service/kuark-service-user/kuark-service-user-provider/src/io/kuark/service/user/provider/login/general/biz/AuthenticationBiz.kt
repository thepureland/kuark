package io.kuark.service.user.provider.login.general.biz

import io.kuark.service.user.provider.login.general.ibiz.IAuthenticationBiz


open class AuthenticationBiz : IAuthenticationBiz {


    override fun login(map: Map<String, Any>) {
//        val env = DefaultEnvironment()
//        SecurityUtils.setSecurityManager(env.securityManager)
//        val principalsTypeCode = map["principalsTypeCode"] as String
//        val principalsType = PrincipalType.valueOf(principalsTypeCode)
//        val principalsIdentifier = map["principalsIdentifier"] as String
//        val credentials = map["credentials"] as String
//        var token: AuthenticationToken
////        when(principalsType) {
////            PrincipalsType.SYS_ACCOUT -> {
////                token = UsernamePasswordToken(principalsIdentifier, credentials)
////            }
////        }
//        token = PrincipalToken(principalsIdentifier, credentials, principalsType)
//        val subject = SecurityUtils.getSubject()
//        subject.login(token)
//
//
//        map["credentials"]


    }

    override fun logout() {
        TODO("Not yet implemented")
    }

}