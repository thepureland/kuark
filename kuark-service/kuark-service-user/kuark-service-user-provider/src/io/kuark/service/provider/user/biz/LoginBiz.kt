package io.kuark.service.provider.user.biz

import io.kuark.ability.auth.third.core.AuthParam
import io.kuark.ability.auth.third.core.IThirdPartyAuthBizFactory
import io.kuark.base.lang.string.RandomStringKit
import io.kuark.base.lang.string.toMd5HexStr
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.user.ibiz.ILoginBiz
import io.kuark.service.user.ibiz.IUserAccountBiz
import io.kuark.service.user.ibiz.IUserAccountThirdPartyBiz
import io.kuark.service.user.po.UserAccount
import io.kuark.service.user.po.UserAccountThirdParty
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
    private lateinit var userAccountThirdPartyBiz: IUserAccountThirdPartyBiz

    @Autowired
    private lateinit var userAuthBiz: IUserAccountBiz


    fun login() {

    }

    override fun thirdLogin(type: String, code: String, state: String) {
        val authBiz = thirdPartyAuthBizFactory.getAuthBiz(type)
        val context = KuarkContextHolder.get()
        val subSysCode = context.subSysCode
        val ownerId = context.ownerId

        // 获取第三方token
        val authParamBuilder = AuthParam.Builder().code(code).state(state)
        val token = authBiz.getToken(authParamBuilder)
        token ?: error("获取第三方token失败！")

        // 获取用户在第三方的信息
        val thirdPartyUser = authBiz.getUserInfo(token.accessToken)
        thirdPartyUser ?: error("获取用户在第三方的信息失败！")

        // 如果第三方账号未在当前系统登陆过，就注册之
        val identifierExists = userAccountThirdPartyBiz.isIdentifierExists(
            type, thirdPartyUser.getOpenId(), subSysCode, ownerId
        )
        if (!identifierExists) {
            var loginAccount = thirdPartyUser.loginAccount()

            // 确保用户名惟一
            while (userAuthBiz.isUsernameExists(loginAccount, subSysCode, ownerId)) {
                loginAccount += RandomStringKit.randomNumeric(3)
            }

            // 由于密码MD5的盐是用用户的id，故先生成id，而不是保存时由数据库自动生成
            val userId = RandomStringKit.uuid()

            // 生成一个随机的定长字符串并使用MD5加密，由于第三方的密码不可用，故随机
            val psd = RandomStringKit.randomNumeric(16).toMd5HexStr(userId)

            val userAccount = UserAccount {
                id = userId
                username = loginAccount
                password = psd
                subSysDictCode = subSysCode
                this.ownerId = ownerId
            }
            // 注册本地用户
            if (userAuthBiz.register(userAccount)) {
                // 将本地用户与OpenId相关联
                val userAccountThirdParty = UserAccountThirdParty {
                    userAccountId = userId
                    identityTypeDictCode = type
                    identifier = thirdPartyUser.getOpenId()
                    subSysDictCode = subSysCode
                    this.ownerId = ownerId
                }
                userAccountThirdPartyBiz.save(userAccountThirdParty)
            }
        }
    }

}