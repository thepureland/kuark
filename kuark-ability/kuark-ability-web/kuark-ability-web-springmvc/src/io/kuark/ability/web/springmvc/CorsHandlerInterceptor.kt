package io.kuark.ability.web.springmvc

import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


open class CorsHandlerInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"))
        response.setHeader("Access-Control-Allow-Methods", "*")
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader(
            "Access-Control-Allow-Headers",
            "Authorization,Origin, X-Requested-With, Content-Type, Accept,Access-Token"
        )
        return true
    }

}