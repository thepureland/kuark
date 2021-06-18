package io.kuark.ability.workflow

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import java.util.*
import java.util.stream.Collectors


@Configuration
open class DemoApplicationConfiguration {

    private val logger = LoggerFactory.getLogger(DemoApplicationConfiguration::class.java)

    @Bean
    open fun myUserDetailsService(): UserDetailsService {
        val inMemoryUserDetailsManager = InMemoryUserDetailsManager()
        val usersGroupsAndRoles = arrayOf(
            arrayOf("salaboy", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"),
            arrayOf("ryandawsonuk", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"),
            arrayOf("erdemedeiros", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"),
            arrayOf("other", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"),
            arrayOf("admin", "password", "ROLE_ACTIVITI_ADMIN")
        )
        for (user in usersGroupsAndRoles) {
            val authoritiesStrings = Arrays.asList(*Arrays.copyOfRange(user, 2, user.size))
            logger.info("> Registering new user: " + user[0] + " with the following Authorities[" + authoritiesStrings + "]")
            inMemoryUserDetailsManager.createUser(
                User(
                    user[0], passwordEncoder().encode(user[1]),
                    authoritiesStrings.stream().map { s: String? ->
                        SimpleGrantedAuthority(
                            s
                        )
                    }.collect(Collectors.toList())
                )
            )
        }
        return inMemoryUserDetailsManager
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}