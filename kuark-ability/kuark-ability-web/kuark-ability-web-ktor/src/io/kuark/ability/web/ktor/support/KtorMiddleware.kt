package io.kuark.ability.web.ktor.support

import io.ktor.application.*

interface KtorMiddleware {

    /**
     * 注册中间件
     */
    fun Application.middleware()

}