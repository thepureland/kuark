package io.kuark.ability.auth.third.core

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
interface IAuthUser {

    fun loginAccount(): String

    fun getOpenId(): String

    fun getAvatarUrl(): String?

}