package io.kuark.ability.auth.context

import io.kuark.ability.auth.rbac.dao.AuthUserAccountDao
import io.kuark.ability.auth.rbac.ibiz.IAuthUserAccountBiz
import io.kuark.ability.auth.rbac.ibiz.IAuthUserAccountThirdPartyBiz
import io.kuark.ability.auth.rbac.model.po.AuthUserAccount
import org.ktorm.entity.first
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import javax.security.auth.login.AccountException

open class UserAccountDetailsBiz: UserDetailsService {

    @Autowired
    private lateinit var authUserAccountBiz: IAuthUserAccountBiz

    @Autowired
    private lateinit var authUserAccountThirdPartyBiz: IAuthUserAccountThirdPartyBiz

    override fun loadUserByUsername(username: String): UserDetails {
        val userAccount = authUserAccountBiz.getByUsername(username) ?: throw AccountException("用户名或密码不正确！")
        return UserAccountDetails(userAccount)
    }

}