package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ILoginBizProxy
import org.springframework.stereotype.Component

@Component
class LoginFallback: ILoginBizProxy {

    override fun githubLogin(code: String, state: String) {
//        return Result("调用失败，服务被降级", 500)
    }

}