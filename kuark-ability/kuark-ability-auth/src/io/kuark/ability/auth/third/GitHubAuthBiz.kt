package io.kuark.ability.auth.third

import org.springframework.stereotype.Service


/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Service
open class GitHubAuthBiz: IAuthBiz {

    companion object {
        private const val AUTH_URL = "https://github.com/login/oauth/authorize?client_id=${0}&redirect_uri=${1}&state=${2}"

        private const val GET_TOKEN_URL = "https://github.com/login/oauth/access_token"
        private const val GET_USER_URL = "https://api.github.com/user"

        private const val CLIENT_ID = "620413e00b95a0e63eda"
        private const val CLIENT_SECRET = "a64e7e0ac14f86802c8be1663cabbbc620affc38"
        private const val REDIRECT_URI = "http://localhost:8080/oauth/github/callback"
    }

    override fun getAuthUrl(authParam: AuthParam): String {

        TODO("Not yet implemented")
    }

    override fun getToken(authParam: AuthParam): String {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(token: String): AuthUserVO? {
        TODO("Not yet implemented")
    }

    override fun checkIsExistsOpenId(openId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun storeOpenIdByUser(openId: String, userId: Int?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getUserNameByOpenId(openId: String): String? {
        TODO("Not yet implemented")
    }

}

fun main() {
    val AUTH_URL = "https://github.com/login/oauth/authorize?client_id=${0}&redirect_uri=${1}&state=${2}"
    println(AUTH_URL.format("clientId", "rUri", "s"))
}