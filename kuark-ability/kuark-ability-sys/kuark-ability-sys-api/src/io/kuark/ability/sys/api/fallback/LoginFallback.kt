package io.kuark.ability.sys.api.fallback

import io.kuark.ability.sys.api.proxy.ILoginBizProxy
import org.springframework.stereotype.Component

@Component
class LoginFallback: ILoginBizProxy {

    override fun githubLogin(code: String, state: String) {
//        return Result("调用失败，服务被降级", 500)
    }

}