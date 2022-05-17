package io.kuark.ability.web.springmvc

import io.kuark.ability.web.springmvc.kit.getBrowserInfo
import io.kuark.ability.web.springmvc.kit.getOsInfo
import io.kuark.ability.web.springmvc.kit.getRemoteIp
import io.kuark.context.core.ClientInfo
import io.kuark.context.core.IContextInitializer
import io.kuark.context.core.KuarkContext
import io.kuark.context.core.KuarkContextHolder
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


open class WebContextInitFilter: IWebContextInitFilter {

    @Autowired
    private lateinit var webContextInitializer: IContextInitializer

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val context = KuarkContext()

        // session
        val session = (request as HttpServletRequest).session
        session.attributeNames.asIterator().forEach { name ->
            val value = session.getAttribute(name)
            context.addSessionAttributes(Pair(name, value))
        }

        // cookie
        request.cookies?.forEach { cookie ->
            context.addCookieAttributes(Pair(cookie.name, cookie.value))
        }

        // header
        request.headerNames.asIterator().forEach { name ->
            val value = request.getHeader(name)
            context.addHeaderAttributes(Pair(name, value))
        }

        // client info
        val clientInfoBuilder = ClientInfo.Builder()
        clientInfoBuilder.ip(request.getRemoteIp())
        clientInfoBuilder.domain(request.serverName)
        clientInfoBuilder.url(request.requestURI)
        clientInfoBuilder.params(request.parameterMap)
        clientInfoBuilder.browser(request.getBrowserInfo())
        clientInfoBuilder.os(request.getOsInfo())
        clientInfoBuilder.requestReferer(request.getHeader("referer"))
        clientInfoBuilder.locale(request.locale)
//        clientInfoBuilder.timeZone() //TODO
        context.clientInfo = ClientInfo(clientInfoBuilder)


        // 初始化上下文
        webContextInitializer.init(context)
        KuarkContextHolder.set(context)

        chain.doFilter(request, response)
    }


}