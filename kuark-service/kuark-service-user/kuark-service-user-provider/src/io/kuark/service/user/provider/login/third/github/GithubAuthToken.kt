package io.kuark.service.user.provider.login.third.github

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.kuark.service.user.provider.login.third.core.IAuthToken

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubAuthToken(

    @get:JsonProperty("access_token")
    override val accessToken: String,

    @get:JsonProperty("token_type")
    override val  tokenType: String,

    override val  scope: String?
): IAuthToken {

    constructor(): this("", "", null)

//    override val accessToken: String
//        get() = accessToken
//
//    override fun getTokenType(): String = tokenType
//
//    override fun getScope(): String? = scope

}