package io.kuark.service.user.consumer.controller

import io.kuark.service.user.consumer.proxy.ILoginBizProxy
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@RestController
class LoginFeignController {

    private lateinit var loginBizProxy: ILoginBizProxy

    @GetMapping("/thirdPartyLogin/github")
    fun githubLogin(@RequestParam code: String, @RequestParam state: String) {
        loginBizProxy.githubLogin(code, state)
    }


}