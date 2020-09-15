package io.kuark.ability.web.support

import io.ktor.application.Application

interface KtorMiddleware {

    /**
     * 注册中间件
     */
    fun Application.middleware()

}