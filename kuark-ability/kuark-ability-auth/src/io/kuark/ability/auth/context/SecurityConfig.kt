package io.kuark.ability.auth.context

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
open class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userAccountDetailsBiz: UserAccountDetailsBiz

    /**
     * 认证
     */
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userAccountDetailsBiz).passwordEncoder(passwordEncoder());
    }

    /**
     * 授权
     */
    override fun configure(http: HttpSecurity) { //TODO
        http.authorizeRequests()
            .antMatchers("/user", "/menu")
            .hasRole("ADMIN")
            .antMatchers("/", "/**").permitAll()
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/home")
            .and()
            .logout()
            .logoutSuccessUrl("/login")
    }

    private fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder() //TODO
    }


}