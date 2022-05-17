package io.kuark.service.user.provider.login.general

import io.kuark.context.core.KuarkContext
import io.kuark.context.core.KuarkContextHolder
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails

open class AdditionalAuthenticationProvider : DaoAuthenticationProvider() {

    override fun additionalAuthenticationChecks(
        userDetails: UserDetails, authentication: UsernamePasswordAuthenticationToken
    ) {
        val verifyCode = KuarkContextHolder.get().otherInfos?.get(KuarkContext.OTHER_INFO_KEY_VERIFY_CODE)
        val code = KuarkContextHolder.get().sessionAttributes?.get("verify_code")
//        if (verifyCode == null || code == null || verifyCode != code) {
//            throw AuthenticationServiceException("验证码错误！")
//        }

        super.additionalAuthenticationChecks(userDetails, authentication)
    }

}