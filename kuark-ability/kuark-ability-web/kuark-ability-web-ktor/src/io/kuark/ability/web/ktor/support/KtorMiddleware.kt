package io.kuark.ability.web.ktor.support

import io.ktor.application.Application

interface KtorMiddleware {

    /**
     * 注册中间件
     */
    fun Application.middleware()

}