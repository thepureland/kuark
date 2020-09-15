package io.kuark.auth.third

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
data class AuthTokenVO(
    val accessToken: String,
    val tokenType: String,
    val scope: String?
)