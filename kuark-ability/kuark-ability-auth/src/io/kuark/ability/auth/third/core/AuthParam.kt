package io.kuark.ability.auth.third.core

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
class AuthParam private constructor(builder: Builder) {

    var clientId: String? = null

    var clientSecret: String? = null

    var redirectUri: String? = null

    var state: String? = null

    var code: String? = null

    init {
        clientId = builder.clientId
        clientSecret = builder.clientSecret
        redirectUri = builder.redirectUri
        state = builder.state
        code = builder.code
    }

    class Builder {

        internal var clientId: String? = null

        internal var clientSecret: String? = null

        internal var redirectUri: String? = null

        internal var state: String? = null

        internal var code: String? = null

        fun build(): AuthParam = AuthParam(this)

        fun clientId(clientId: String): Builder {
            this.clientId = clientId
            return this
        }

        fun clientSecret(clientSecret: String): Builder {
            this.clientSecret = clientSecret
            return this
        }

        fun redirectUri(redirectUri: String): Builder {
            this.redirectUri = redirectUri
            return this
        }

        fun state(state: String): Builder {
            this.state = state
            return this
        }

        fun code(code: String): Builder {
            this.code = code
            return this
        }

    }


}