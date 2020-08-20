package io.kuark.web.support

import io.ktor.routing.Route

interface KtorRouter {

    /**
     * 注册路由
     */
    fun Route.router()

}