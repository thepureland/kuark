package io.kuark.ability.auth.context

import io.kuark.ability.auth.login.general.PrincipalType
import org.apache.shiro.authc.UsernamePasswordToken

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
class PrincipalToken(username: String, password: String, val principalType: PrincipalType) :
    UsernamePasswordToken(username, password)