package io.kuark.ability.workflow

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component


@Component
class SecurityUtil {

    private val logger = LoggerFactory.getLogger(SecurityUtil::class.java)

    @Autowired
    private val userDetailsService: UserDetailsService? = null
    fun logInAs(username: String) {
        val user = userDetailsService!!.loadUserByUsername(username)
            ?: throw IllegalStateException("User $username doesn't exist, please provide a valid user")
        logger.info("> Logged in as: $username")
        SecurityContextHolder.setContext(SecurityContextImpl(object : Authentication {
            override fun getAuthorities(): Collection<GrantedAuthority?> {
                return user.authorities
            }

            override fun getCredentials(): Any {
                return user.password
            }

            override fun getDetails(): Any {
                return user
            }

            override fun getPrincipal(): Any {
                return user
            }

            override fun isAuthenticated(): Boolean {
                return true
            }

            @Throws(IllegalArgumentException::class)
            override fun setAuthenticated(isAuthenticated: Boolean) {
            }

            override fun getName(): String {
                return user.username
            }
        }))
        org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(username)
    }
}