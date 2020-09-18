package io.kuark.service.user.biz

import io.kuark.ability.auth.third.core.AuthParam
import io.kuark.ability.auth.third.core.IThirdPartyAuthBizFactory
import io.kuark.context.core.KuarkContext
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.user.ibiz.ILoginBiz
import io.kuark.service.user.ibiz.IUserAccountThirdPartyAuthBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 用户登录业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
class LoginBiz : ILoginBiz {

    @Autowired
    private lateinit var thirdPartyAuthBizFactory: IThirdPartyAuthBizFactory

    @Autowired
    private lateinit var userAccountThirdPartyAuthBiz: IUserAccountThirdPartyAuthBiz


    fun login() {

    }

    fun thirdLogin(type: String, code: String, state: String) {
        val authBiz = thirdPartyAuthBizFactory.getAuthBiz(type)

        // 获取第三方token
        val authParamBuilder = AuthParam.Builder().code(code).state(state)
        val token = authBiz.getToken(authParamBuilder)
        token ?: error("获取第三方token失败！")

        // 获取用户在第三方的信息
        val userInfo = authBiz.getUserInfo(token.accessToken)
        userInfo ?: error("获取用户在第三方的信息失败！")

        // 如果第三方账号未在当前系统登陆过，就注册之
        val context = KuarkContextHolder.get()
        val identifierExists = userAccountThirdPartyAuthBiz.isIdentifierExists(
            type, userInfo.getOpenId(), context.subSysCode, context.ownerId
        )
        if (!identifierExists) {

        }


    }

}