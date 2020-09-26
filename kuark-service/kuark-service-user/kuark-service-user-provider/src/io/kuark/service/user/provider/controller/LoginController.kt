package io.kuark.service.user.provider.controller

import io.kuark.service.user.provider.ibiz.ILoginBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

    @Autowired
    private lateinit var loginBiz: ILoginBiz


    @GetMapping("/thirdPartyLogin/github")
    fun githubLogin(code: String, state: String) {
        loginBiz.thirdLogin("github", code, state)
    }

}