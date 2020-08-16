package org.kuark.auth.context

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import java.util.*


/**
 *
 *
 * @author K
 * @since 1.0.0
 */
class AuthRealm: AuthorizingRealm() {

    /**
     * 身份认证。即登陆时通过账号和密码验证登陆人的身份信息
     */
    override fun doGetAuthenticationInfo(authenticationToken: AuthenticationToken): AuthenticationInfo {
//        println("-------身份认证方法--------")
//        val userName = authenticationToken.principal
//        val userPwd = String((authenticationToken.credentials as CharArray)!!)
//
//        //根据用户名从数据库获取密码
//        val password = "123"
//        if (userName == null) {
//            throw AccountException("用户名不正确")
//        } else if (userPwd != password) {
//            throw AccountException("密码不正确")
//        }
//        return SimpleAuthenticationInfo(userName, password, name)
        TODO("注入")
    }

    /**
     * 权限认证，即登录过后，每个身份不一定，对应的所能看的页面也不一样
     */
    override fun doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo {
//        val username = SecurityUtils.getSubject().principal as String
//        val info = SimpleAuthorizationInfo()
//        val stringSet: MutableSet<String> = HashSet()
//        stringSet.add("user:show")
//        stringSet.add("user:admin")
//        info.stringPermissions = stringSet
//        return info
        TODO("注入")
    }

}