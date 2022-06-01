package io.kuark.service.user.provider.context

import com.fasterxml.jackson.databind.ObjectMapper
import io.kuark.base.log.LogFactory
import io.kuark.base.net.http.HttpResult
import io.kuark.context.core.KuarkContext
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.user.provider.user.biz.ibiz.IUserAccountBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.*
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


open class DefaultLoginFilter : UsernamePasswordAuthenticationFilter() {

    private val log = LogFactory.getLog(this::class)

    @Autowired
    private lateinit var userAccountBiz: IUserAccountBiz

    @PostConstruct
    protected fun init() {
        setAuthenticationSuccessHandler(createAuthenticationSuccessHandler())
        setAuthenticationFailureHandler(createAuthenticationFailureHandler())
        setFilterProcessesUrl("/doLogin")
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        if (!request.method.equals("POST")) {
            throw AuthenticationServiceException("Authentication method not supported: $request.method")
        }
        val session = request.session
        val verifyCode = session.getAttribute("verify_code") as String?
        // 不同浏览器上面getContentType()可能是大写可能是小写
        return if (request.contentType.equals(MediaType.APPLICATION_JSON_VALUE)
            || request.contentType.equals(MediaType.APPLICATION_JSON_UTF8_VALUE, true)
        ) {
            var loginData: Map<String?, String?> = HashMap()
            try {
                loginData = ObjectMapper().readValue(request.inputStream, loginData::class.java)
            } catch (e: IOException) {
                log.error("读取登录信息错误!", e)
            } finally {
//                val code = loginData["code"]
//                checkCode(response, code, verifyCode)
            }
            val username = loginData[usernameParameter]?.trim { it <= ' ' }
            val password = loginData[passwordParameter]
            val authRequest = UsernamePasswordAuthenticationToken(username, password)
            setDetails(request, authRequest)
            val authentication = authenticationManager.authenticate(authRequest)
            val subSysCode = KuarkContextHolder.get().subSysCode!!
            val user = userAccountBiz.getByUsername(subSysCode, username!!)
            session.setAttribute(KuarkContext.SESSION_KEY_USER, user)
            authentication
        } else {
//            checkCode(response, request.getParameter("code"), verifyCode)
            super.attemptAuthentication(request, response)
        }
    }

    protected fun createAuthenticationSuccessHandler(): AuthenticationSuccessHandler {
        return AuthenticationSuccessHandler { _, response, authentication ->
            val principal = authentication.principal
            response.contentType = "application/json;charset=utf-8"
            val out = response.writer
            out.write(ObjectMapper().writeValueAsString(principal))
            out.flush()
            out.close()
        }
    }

    protected fun createAuthenticationFailureHandler(): AuthenticationFailureHandler {
        return AuthenticationFailureHandler { _, resp, e ->
            resp.contentType = "application/json;charset=utf-8"
            val out = resp.writer
            val errMsg = when (e) {
                is LockedException -> "账户被锁定，请联系管理员!"
                is CredentialsExpiredException -> "密码过期，请联系管理员!"
                is AccountExpiredException -> "账户过期，请联系管理员!"
                is DisabledException -> "账户被禁用，请联系管理员!"
                is BadCredentialsException -> "用户名或密码错误！"
                else -> e.message
            }
            out.write(ObjectMapper().writeValueAsString(HttpResult.error(errMsg ?: "")))
            out.flush()
            out.close()
        }
    }

    private fun checkCode(resp: HttpServletResponse?, code: String?, verify_code: String?) {
        if (code == null || verify_code == null || "" == code || verify_code.lowercase() != code.lowercase()) {
            //验证码不正确
            throw AuthenticationServiceException("验证码不正确")
        }
    }

}