package io.kuark.service.user.provider.context

import io.kuark.service.user.provider.user.model.enums.UserAccountStatus
import io.kuark.service.user.provider.user.model.po.UserAccount
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserAccountDetails(val userAccount: UserAccount): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }

    override fun getPassword(): String = userAccount.password

    override fun getUsername(): String = userAccount.username

    override fun isAccountNonExpired(): Boolean = userAccount.userStatusDictCode != UserAccountStatus.EXPIRED.code

    override fun isAccountNonLocked(): Boolean = userAccount.userStatusDictCode != UserAccountStatus.LOCKED.code

    override fun isCredentialsNonExpired() = userAccount.userStatusDictCode != UserAccountStatus.CREDENTIAL_EXPIRED.code

    override fun isEnabled(): Boolean = userAccount.userStatusDictCode == UserAccountStatus.NORMAL.code

}