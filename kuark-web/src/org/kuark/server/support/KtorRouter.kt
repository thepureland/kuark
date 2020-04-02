package org.kuark.server.support

import io.ktor.routing.Route

interface KtorRouter {

    /**
     * 注册路由
     */
    fun Route.router()

}