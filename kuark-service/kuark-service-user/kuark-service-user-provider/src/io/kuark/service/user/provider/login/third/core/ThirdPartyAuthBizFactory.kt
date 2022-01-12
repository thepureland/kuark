package io.kuark.service.user.provider.login.third.core

import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
class ThirdPartyAuthBizFactory : IThirdPartyAuthBizFactory {

    @Resource(name = "gitHubAuthBiz")
    private lateinit var gitHubAuthBiz: IAuthBiz

    override fun getAuthBiz(type: String): IAuthBiz =
        when (type) {
            "github" -> gitHubAuthBiz
            else -> error("不支持的第三方登陆授权类型【$type】！")
        }

}