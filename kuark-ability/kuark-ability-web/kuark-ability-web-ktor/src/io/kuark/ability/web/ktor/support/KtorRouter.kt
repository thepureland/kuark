package io.kuark.ability.web.ktor.support

import io.ktor.routing.*

interface KtorRouter {

    /**
     * 注册路由
     */
    fun Route.router()

}