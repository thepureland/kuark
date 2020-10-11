package io.kuark.ability.auth.login.third.core

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
interface IAuthToken {

    val accessToken: String

    val tokenType: String

    val scope: String?

}