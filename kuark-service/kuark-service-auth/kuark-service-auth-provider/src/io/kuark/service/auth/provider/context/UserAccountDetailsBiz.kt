package io.kuark.service.auth.provider.context

import io.kuark.service.auth.provider.rbac.ibiz.IAuthUserAccountBiz
import io.kuark.service.auth.provider.rbac.ibiz.IAuthUserAccountThirdPartyBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import javax.security.auth.login.AccountException

/**
 * 用户账号明细业务，作为认证数据源
 *
 * @author K
 * @since 1.0.0
 */
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