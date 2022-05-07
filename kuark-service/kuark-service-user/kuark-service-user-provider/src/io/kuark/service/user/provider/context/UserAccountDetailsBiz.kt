package io.kuark.service.user.provider.context

import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.user.provider.user.biz.ibiz.IUserAccountBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import javax.security.auth.login.AccountException

/**
 * 用户账号明细业务，作为认证数据源
 *
 * @author K
 * @since 1.0.0
 */
open class UserAccountDetailsBiz : UserDetailsService {

    @Autowired
    private lateinit var userAccountBiz: IUserAccountBiz

//    @Autowired
//    private lateinit var userAccountThirdBiz: IUserAccountThirdBiz

    override fun loadUserByUsername(username: String): UserDetails {
        val subSysCode = KuarkContextHolder.get().subSysCode!!
        val userAccount = userAccountBiz.getByUsername(subSysCode, username) ?: throw AccountException("用户名或密码不正确！")
        val roleIds = userAccountBiz.getPermissions(userAccount.id!!)
        val roles = roleIds.map { SimpleGrantedAuthority("ROLE_$it") }
        return UserAccountDetails(userAccount, roles)
    }

}