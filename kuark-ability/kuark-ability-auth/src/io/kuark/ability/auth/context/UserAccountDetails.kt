package io.kuark.ability.auth.context

import io.kuark.ability.auth.rbac.model.po.AuthUserAccount
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserAccountDetails(val authUserAccount: AuthUserAccount): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }

    override fun getPassword(): String = authUserAccount.password

    override fun getUsername(): String = authUserAccount.username

    override fun isAccountNonExpired(): Boolean = authUserAccount.isActive //TODO

    override fun isAccountNonLocked(): Boolean = authUserAccount.isActive //TODO

    override fun isCredentialsNonExpired() = authUserAccount.isActive //TODO

    override fun isEnabled(): Boolean = authUserAccount.isActive

}