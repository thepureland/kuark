package io.kuark.ability.auth.context

import com.fasterxml.jackson.databind.ObjectMapper
import io.kuark.base.net.http.HttpResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.*
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.io.PrintWriter


@Configuration
@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
open class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userAccountDetailsBiz: UserAccountDetailsBiz

    /**
     * 配置认证
     */
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userAccountDetailsBiz).passwordEncoder(passwordEncoder())
    }

    /**
     * 配置授权
     */
    override fun configure(http: HttpSecurity) { //TODO
//        http.rememberMe().key("KUARK-auth-Key")

        http.authorizeRequests()
            .antMatchers("/rememberme").rememberMe()
            .antMatchers("/user", "/menu")
            .hasRole("ADMIN")
            .antMatchers("/", "/**").permitAll()
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

            .logout()
//            .logoutRequestMatcher(AntPathRequestMatcher("/logout", "POST"))
            .logoutSuccessUrl("/login")

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

    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**")   //TODO
    }

    private fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder() //TODO
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

}