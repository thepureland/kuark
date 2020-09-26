package io.kuark.ability.web.ktor.support

import io.ktor.routing.Route

interface KtorRouter {

    /**
     * 注册路由
     */
    fun Route.router()

}