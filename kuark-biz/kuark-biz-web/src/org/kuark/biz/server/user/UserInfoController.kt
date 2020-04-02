package org.kuark.biz.server.user

import org.kuark.server.support.KtorRouter
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.kuark.biz.iservice.user.IUserInfoService
import org.kuark.biz.model.user.UserInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
class UserInfoController : KtorRouter {

    @Autowired
    lateinit var userInfoService: IUserInfoService

    override fun Route.router() {
        route("/api") {
            // 访问: /api/group
            get("/group") {
                testLog(call)
            }
        }
        get("/add") {

            val userInfo = UserInfo {
                name = call.request.queryParameters["name"].toString()
            }

            val id = userInfoService.add(userInfo)
            call.respondText(id.toString())
        }

        get("/") {
//            call.info("@@@ ==== call log 这是测试!
            print("####  total: " + userInfoService.all().size)
            call.respondText("测试1111")
        }
    }

    private fun testLog(call: ApplicationCall) {
        // ...实现逻辑
    }
}