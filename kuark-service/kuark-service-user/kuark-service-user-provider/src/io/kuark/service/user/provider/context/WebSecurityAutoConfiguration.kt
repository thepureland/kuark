package io.kuark.service.user.provider.context

import com.fasterxml.jackson.databind.ObjectMapper
import io.kuark.base.net.http.HttpResult
import io.kuark.service.user.provider.login.general.AdditionalAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.*
import org.springframework.security.config.annotation.ObjectPostProcessor
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.security.web.firewall.StrictHttpFirewall
import org.springframework.security.web.session.HttpSessionEventPublisher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.io.PrintWriter


@Configuration
@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityAutoConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userAccountDetails: UserDetailsService

    @Autowired
    private lateinit var customAccessDecisionManager: CustomAccessDecisionManager

    @Autowired
    private lateinit var customFilterInvocationSecurityMetadataSource: CustomFilterInvocationSecurityMetadataSource

    /**
     * 配置认证
     */
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userAccountDetails).passwordEncoder(passwordEncoder())
    }

    /**
     * 配置授权
     */
    override fun configure(http: HttpSecurity) { //TODO
        http.cors()

        http.authorizeRequests()
            .withObjectPostProcessor(object : ObjectPostProcessor<FilterSecurityInterceptor> {

                override fun <O : FilterSecurityInterceptor> postProcess(o: O): O {
                    o.securityMetadataSource = customFilterInvocationSecurityMetadataSource
                    o.accessDecisionManager = customAccessDecisionManager
                    return o;
                }

            })
            .antMatchers("/rememberme").rememberMe() //rememberme 接口，必须是通过自动登录认证后才能访问，如果用户是通过用户名/密码认证的，则无法访问该接口
//            .antMatchers("/", "/**").permitAll()
            .and()
            .formLogin()
            .loginPage("/login")
//            .loginProcessingUrl("/login")
//            .usernameParameter()
//            .usernameParameter()
//            .successForwardUrl() // 服务端跳转： 不管从哪来，登陆成功后始终跳到指定页面
//            .defaultSuccessUrl("/home") // 重定向：从哪个页面来，登陆成功后跳到哪个页面
            .successHandler { _, resp, authentication ->
                val principal: Any = authentication.principal
                resp.contentType = "application/json;charset=utf-8"
                val out = resp.writer
                out.write(ObjectMapper().writeValueAsString(principal))
                out.flush()
                out.close()
            }
            .failureHandler { _, resp, e ->
                resp.contentType = "application/json;charset=utf-8"
                val out: PrintWriter = resp.writer
                val errMsg = when (e) {
                    is LockedException -> "账户被锁定，请联系管理员!"
                    is CredentialsExpiredException -> "密码过期，请联系管理员!"
                    is AccountExpiredException -> "账户过期，请联系管理员!"
                    is DisabledException -> "账户被禁用，请联系管理员!"
                    is BadCredentialsException -> "用户名或者密码输入错误，请重新输入!"
                    else -> e.message
                }
                out.write(ObjectMapper().writeValueAsString(HttpResult.error(errMsg ?: "")))
                out.flush()
                out.close()
            }

            .and()
            .rememberMe()
            .key("KUARK-user-Key")  // 若没有设置 key，key 默认值是一个 UUID 字符串，这样会带来一个问题：如果服务端重启，这个 key 会变，这样就导致之前派发出去的所有 remember-me 自动登录令牌失效
            .tokenRepository(tokenRepository())
            .and()
            .logout()
//            .logoutRequestMatcher(AntPathRequestMatcher("/logout", "POST"))
            .logoutSuccessUrl("/login")
            .and()
            .sessionManagement()
            .maximumSessions(1) // 配置最大会话数为 1，这样后面的登录就会自动踢掉前面的登录
//            .maxSessionsPreventsLogin(true)  // 添加该配置后，将禁止新的登录

        http.exceptionHandling()
            .authenticationEntryPoint { _, resp, _ ->
                resp.contentType = "application/json;charset=utf-8"
                val out = resp.writer
                out.write("尚未登录，请先登录")
                out.flush()
                out.close()
            }
            .accessDeniedHandler { _, resp, _ ->
                resp.contentType = "application/json;charset=utf-8"
                val out = resp.writer
                out.write("没有访问权限!")
                out.flush()
                out.close()
            }
        http.csrf().disable() //TODO
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**")   //TODO
    }

    @Bean
    @ConditionalOnMissingBean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder() // 官方推荐方式，自带盐
    }

    /**
     * 角色继承
     */
    @Bean
    @ConditionalOnMissingBean
    open fun roleHierarchy(): RoleHierarchy {
        val hierarchy = RoleHierarchyImpl()
        hierarchy.setHierarchy("ROLE_admin > ROLE_user") //TODO
        return hierarchy
    }

    @Bean
    @ConditionalOnMissingBean
    open fun userAccountDetailsBiz(): UserAccountDetailsBiz = UserAccountDetailsBiz()

    @Bean
    @ConditionalOnMissingBean
    open fun tokenRepository(): TokenRepository = TokenRepository()

    @Bean
    @ConditionalOnMissingBean
    open fun additionalAuthenticationProvider(): AuthenticationProvider {
        val authenticationProvider = AdditionalAuthenticationProvider()
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        authenticationProvider.setUserDetailsService(UserAccountDetailsBiz())
        return authenticationProvider
    }

    @Bean
    @ConditionalOnMissingBean
    override fun authenticationManager(): AuthenticationManager {
        return ProviderManager(additionalAuthenticationProvider())
    }

    /**
     * 可以将 session 创建以及销毁的事件及时感知到，并且调用 Spring 中的事件机制将相关的创建和销毁事件发布出去，进而被 Spring Security 感知到
     */
    @Bean
    @ConditionalOnMissingBean
    open fun httpSessionEventPublisher(): HttpSessionEventPublisher = HttpSessionEventPublisher()

    @Bean
    @ConditionalOnMissingBean
    open fun sessionRegistry(): SessionRegistryImpl = SessionRegistryImpl()

    @Bean
    @ConditionalOnMissingBean
    open fun httpFirewall(): HttpFirewall {
        val firewall = StrictHttpFirewall()
        firewall.setUnsafeAllowAnyHttpMethod(true)
//        firewall.setAllowSemicolon(true)
        return firewall
    }

//    @Bean
//    open fun corsConfigurationSource(): CorsConfigurationSource {
//        val configuration = CorsConfiguration()
//        configuration.allowedOrigins = listOf("http://localhost:8080")
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/hello", configuration)
//        return source
//    }


//    @Bean
//    @Throws(Exception::class)
//    open fun loginFilter(): LoginFilter {
//        val loginFilter = LoginFilter()
//        loginFilter.setAuthenticationSuccessHandler { request, response, authentication ->
//            response.setContentType("application/json;charset=utf-8")
//            val out: PrintWriter = response.getWriter()
//            val hr: Hr = authentication.getPrincipal() as Hr
//            hr.setPassword(null)
//            val ok: RespBean = RespBean.ok("登录成功!", hr)
//            val s = ObjectMapper().writeValueAsString(ok)
//            out.write(s)
//            out.flush()
//            out.close()
//        }
//        loginFilter.setAuthenticationFailureHandler { request, response, exception ->
//            response.setContentType("application/json;charset=utf-8")
//            val out: PrintWriter = response.getWriter()
//            val respBean: RespBean = RespBean.error(exception.getMessage())
//            if (exception is LockedException) {
//                respBean.setMsg("账户被锁定，请联系管理员!")
//            } else if (exception is CredentialsExpiredException) {
//                respBean.setMsg("密码过期，请联系管理员!")
//            } else if (exception is AccountExpiredException) {
//                respBean.setMsg("账户过期，请联系管理员!")
//            } else if (exception is DisabledException) {
//                respBean.setMsg("账户被禁用，请联系管理员!")
//            } else if (exception is BadCredentialsException) {
//                respBean.setMsg("用户名或者密码输入错误，请重新输入!")
//            }
//            out.write(ObjectMapper().writeValueAsString(respBean))
//            out.flush()
//            out.close()
//        }
//        loginFilter.setAuthenticationManager(authenticationManagerBean())
//        loginFilter.setFilterProcessesUrl("/doLogin")
//        val sessionStrategy = ConcurrentSessionControlAuthenticationStrategy(sessionRegistry())
//        sessionStrategy.setMaximumSessions(1)
//        loginFilter.setSessionAuthenticationStrategy(sessionStrategy)
//        return loginFilter
//    }

}