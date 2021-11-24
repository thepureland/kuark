package io.kuark.ability.sys.provider.user.controller

import io.kuark.ability.auth.login.third.ibiz.IThirdPatyLoginBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

    @Autowired
    private lateinit var thirdPatyLoginBiz: IThirdPatyLoginBiz


    @GetMapping("/thirdPartyLogin/github")
    fun githubLogin(code: String, state: String) {
        thirdPatyLoginBiz.login("github", code, state)
    }

}