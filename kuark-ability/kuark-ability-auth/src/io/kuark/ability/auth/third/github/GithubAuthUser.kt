package io.kuark.ability.auth.third.github

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.kuark.ability.auth.third.core.IAuthUser

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubAuthUser(
    val login: String,
    val id: String,
    private var avatarUrl: String?
): IAuthUser {

    constructor() : this("", "", null)

    override fun loginAccount(): String = login

    override fun getOpenId(): String = id

    @JsonProperty("avatar_url")
    override fun getAvatarUrl(): String? = avatarUrl

    fun setAvatarUrl(avatarUrl: String?) {
        this.avatarUrl = avatarUrl
    }


}