package io.kuark.service.user.provider.context

import com.fasterxml.jackson.databind.ObjectMapper
import io.kuark.ability.web.common.WebResult
import io.kuark.service.user.provider.login.general.AdditionalAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.session.HttpSessionEventPublisher
import org.springframework.web.cors.CorsUtils


@Configuration
@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityAutoConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userAccountDetails: UserDetailsService

    @Autowired
    private lateinit var customAccessDecisionManager: RoleAccessDecisionManager

    @Autowired
    private lateinit var customFilterInvocationSecurityMetadataSource: RequestUrlAuthorityMetadataSource

//    @Autowired
//    private val authenticationProvider: AuthenticationProvider? = null

    @Bean
    @ConditionalOnMissingBean
    open fun webSecurityConfigurer(): IWebSecurityConfigurer = DefaultWebSecurityConfigurer()

    /**
     * 配置认证
     */
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userAccountDetails).passwordEncoder(passwordEncoder())
//        auth.authenticationProvider(authenticationProvider)
    }

    /**
     * 配置授权
     */
    override fun configure(http: HttpSecurity) {
        val configProvider = webSecurityConfigurer()

        http.authorizeRequests()
            .withObjectPostProcessor(object : ObjectPostProcessor<FilterSecurityInterceptor> {

                override fun <O : FilterSecurityInterceptor> postProcess(o: O): O {
                    o.securityMetadataSource = customFilterInvocationSecurityMetadataSource
                    o.accessDecisionManager = customAccessDecisionManager
                    return o;
                }

            })

//            .antMatchers("/rememberme").rememberMe() //rememberme 接口，必须是通过自动登录认证后才能访问，如果用户是通过用户名/密码认证的，则无法访问该接口
//            .antMatchers("/", "/**").permitAll()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers(*configProvider.permissiveUrls()).permitAll()
            .anyRequest().authenticated() // 其他请求需要登录
            .and()
            .formLogin()
            .loginPage(configProvider.loginPage())
            .loginProcessingUrl(configProvider.loginProcessingUrl())
            .usernameParameter(configProvider.usernameParameter())
            .passwordParameter(configProvider.passwordParameter())
            .and()
            .rememberMe()
            .key(configProvider.rememberMeKey())  // 若没有设置 key，key 默认值是一个 UUID 字符串，这样会带来一个问题：如果服务端重启，这个 key 会变，这样就导致之前派发出去的所有 remember-me 自动登录令牌失效
            .tokenRepository(tokenRepository())
            .and()
            .logout()
            .logoutSuccessHandler { _, resp, _ ->
                resp.contentType = "application/json;charset=utf-8"
                val out = resp.writer
                out.write(ObjectMapper().writeValueAsString(WebResult(null, "注销成功！")))
                out.flush()
                out.close()
            }
            .permitAll()
            .and()
            .sessionManagement()
            .maximumSessions(configProvider.maximumSessions()) // 配置最大会话数为 1，这样后面的登录就会自动踢掉前面的登录
            .maxSessionsPreventsLogin(configProvider.maxSessionsPreventsLogin())  // 添加该配置后，将禁止新的登录

        http.exceptionHandling()
            .authenticationEntryPoint { _, resp, _ ->
                resp.sendError(HttpStatus.UNAUTHORIZED.value())
            }
            .accessDeniedHandler { _, resp, _ ->
                resp.sendError(HttpStatus.FORBIDDEN.value())
            }
        http.csrf().disable()

        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    @ConditionalOnMissingBean
    open fun loginFilter(): DefaultLoginFilter {
        return DefaultLoginFilter().apply {
            setAuthenticationManager(authenticationManager())
        }
    }

//    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
//    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()


    override fun configure(web: WebSecurity) {
        val configProvider = webSecurityConfigurer()
        web.ignoring().antMatchers(*configProvider.ignoringUrls())
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
    open fun userAccountDetailsBiz(): UserDetailsService = UserAccountDetailsBiz()

    @Bean
    @ConditionalOnMissingBean
    open fun tokenRepository(): TokenRepository = TokenRepository()

    @Bean
    @ConditionalOnMissingBean
    open fun additionalAuthenticationProvider(): AuthenticationProvider {
        val authenticationProvider = AdditionalAuthenticationProvider()
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        authenticationProvider.setUserDetailsService(userAccountDetailsBiz())
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

//    @Bean
//    @ConditionalOnMissingBean
//    open fun httpFirewall(): HttpFirewall {
//        val firewall = StrictHttpFirewall()
//        firewall.setUnsafeAllowAnyHttpMethod(true)
////        firewall.setAllowSemicolon(true)
//        return firewall
//    }


}

fun main() {
    println(BCryptPasswordEncoder().encode("test12342&*(*&sdfsfksdjfks*&*&234"))
}