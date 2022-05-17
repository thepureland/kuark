package io.kuark.service.user.provider.user.api.frontend

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class LoginController {

//    @Autowired
//    private lateinit var thirdPatyLoginBiz: IThirdPatyLoginBiz


//    @GetMapping("/thirdPartyLogin/github")
//    fun githubLogin(code: String, state: String) {
//        thirdPatyLoginBiz.login("github", code, state)
//    }

    @GetMapping("/login")
    fun toLogin(): String {
        return "尚未登录，请登录!"
    }

}