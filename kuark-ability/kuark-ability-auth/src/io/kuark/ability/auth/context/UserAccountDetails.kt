package io.kuark.ability.auth.context

import io.kuark.ability.auth.rbac.model.enums.UserAccountStatus
import io.kuark.ability.auth.rbac.model.po.AuthUserAccount
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserAccountDetails(val authUserAccount: AuthUserAccount): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }

    override fun getPassword(): String = authUserAccount.password

    override fun getUsername(): String = authUserAccount.username

    override fun isAccountNonExpired(): Boolean = authUserAccount.userStatusDictCode != UserAccountStatus.EXPIRED.code

    override fun isAccountNonLocked(): Boolean = authUserAccount.userStatusDictCode != UserAccountStatus.LOCKED.code

    override fun isCredentialsNonExpired() = authUserAccount.userStatusDictCode != UserAccountStatus.CREDENTIAL_EXPIRED.code

    override fun isEnabled(): Boolean = authUserAccount.userStatusDictCode == UserAccountStatus.NORMAL.code

}