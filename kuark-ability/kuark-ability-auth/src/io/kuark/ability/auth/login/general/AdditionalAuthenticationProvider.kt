package io.kuark.ability.auth.login.general

import io.kuark.context.core.KuarkContextHolder
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails

open class AdditionalAuthenticationProvider : DaoAuthenticationProvider() {

    override fun additionalAuthenticationChecks(
        userDetails: UserDetails, authentication: UsernamePasswordAuthenticationToken
    ) {
        val verifyCode = KuarkContextHolder.get().otherInfos["verifyCode"]
        val code = KuarkContextHolder.get().session.getAttribute<String>("verify_code")
        if (verifyCode == null || code == null || verifyCode != code) {
            throw AuthenticationServiceException("验证码错误！")
        }

        super.additionalAuthenticationChecks(userDetails, authentication)
    }

}