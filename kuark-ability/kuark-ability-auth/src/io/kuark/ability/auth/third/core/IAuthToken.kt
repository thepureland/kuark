package io.kuark.ability.auth.third.core

import com.fasterxml.jackson.annotation.JsonProperty

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