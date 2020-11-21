package io.kuark.ability.auth.context

import io.kuark.ability.auth.login.general.PrincipalType
import io.kuark.ability.auth.rbac.ibiz.IAuthUserAccountBiz
import io.kuark.ability.auth.rbac.ibiz.IAuthUserAccountThirdPartyBiz
import io.kuark.base.security.DigestKit
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AccountException
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.springframework.beans.factory.annotation.Autowired


/**
 *
 *
 * @author K
 * @since 1.0.0
 */
class AuthRealm : AuthorizingRealm() {

    @Autowired
    private lateinit var authUserAccountBiz: IAuthUserAccountBiz

    @Autowired
    private lateinit var authUserAccountThirdPartyBiz: IAuthUserAccountThirdPartyBiz

    /**
     * 身份认证。即登陆时通过账号和密码验证登陆人的身份信息
     */
    override fun doGetAuthenticationInfo(authenticationToken: AuthenticationToken): AuthenticationInfo {
        if (authenticationToken is PrincipalToken) {
            val username = authenticationToken.principal as String
            val origPassword = String((authenticationToken.credentials as CharArray)!!)
            val md5Password = DigestKit.getMD5(origPassword, username)
            val principalType = authenticationToken.principalType

            // 根据用户名从数据库获取账号信息
            when (principalType) {
                PrincipalType.SYS_ACCOUT -> {
                    val userAccount = authUserAccountBiz.getByUsername(username)
                    if (userAccount == null || userAccount.password != md5Password) {
                        throw AccountException("用户名或密码不正确！")
                    }
                }
                PrincipalType.EMAIL, PrincipalType.MOBILE -> {
                    val userAccountThirdParty = authUserAccountThirdPartyBiz.getByPrincipal(principalType, username)
                    if (userAccountThirdParty == null || userAccountThirdParty.credentials != md5Password) {
                        throw AccountException("用户名或密码不正确！")
                    }
                }
            }

            return SimpleAuthenticationInfo(username, md5Password, name)
        }
        error("AuthenticationToken类型错误！")
    }

    /**
     * 权限认证，即登录过后，每个身份不一定，对应的所能看的页面也不一样
     */
    override fun doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo {
        val username = SecurityUtils.getSubject().principal as String
        val info = SimpleAuthorizationInfo()
        val stringSet = mutableSetOf<String>()

        stringSet.add("user:show")
        stringSet.add("user:admin")
        //TODO

        info.stringPermissions = stringSet
        return info
    }

}