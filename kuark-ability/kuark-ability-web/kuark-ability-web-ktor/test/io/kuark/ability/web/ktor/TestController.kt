package io.kuark.ability.web.ktor

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.kuark.ability.web.ktor.session.WebSession
import io.kuark.ability.web.ktor.support.KtorRouter
import org.springframework.stereotype.Controller

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Controller
class TestController : KtorRouter {

    override fun Route.router() {
        route("/api") {
            // 访问: /api/group
            get("/group") {
                call.respondText("HELLO WORLD!")
            }
        }
        get("/") {
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
        get("/login") {
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
        get("/logout") {
            call.sessions.clear("MySession")
        }
        get("/session") {
            val s = call.sessions.get<WebSession>()
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
    }

}