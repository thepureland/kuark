package io.kuark.service.user.controller

import io.ktor.application.*
import io.ktor.routing.*
import io.kuark.ability.web.support.KtorRouter
import io.kuark.service.user.ibiz.ILoginBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
class LoginController : KtorRouter {

    @Autowired
    private lateinit var loginBiz: ILoginBiz

    override fun Route.router() {
        get("/thirdPartyLogin/github") {
            githubLogin(call)
        }
    }

    private fun githubLogin(call: ApplicationCall) {
        val code = call.request.queryParameters["code"]!!
        val state = call.request.queryParameters["state"]!!
        loginBiz.thirdLogin("github", code, state)
    }

}