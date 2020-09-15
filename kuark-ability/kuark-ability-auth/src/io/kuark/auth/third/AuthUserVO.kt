package io.kuark.auth.third

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
data class AuthUserVO(
    val login: String,
    val id: String,
    val avatarUrl: String?
)