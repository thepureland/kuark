package io.kuark.ability.web

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.kuark.ability.web.session.WebSession
import io.kuark.ability.web.support.KtorRouter
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