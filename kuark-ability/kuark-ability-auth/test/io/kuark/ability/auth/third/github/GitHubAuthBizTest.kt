package io.kuark.ability.auth.third.github

import io.kuark.ability.auth.third.core.AuthParam
import org.junit.jupiter.api.Test

internal class GitHubAuthBizTest {

    companion object {
        private const val CLIENT_ID = "620413e00b95a0e63eda"
        private const val CLIENT_SECRET = "a64e7e0ac14f86802c8be1663cabbbc620affc38"
        private const val REDIRECT_URI = "http://localhost:8080/oauth/github/callback"
    }

    @Test
    fun getAuthUrl() {
        val authParam = AuthParam.Builder()
            .clientId(CLIENT_ID)
            .redirectUri(REDIRECT_URI)
            .build()
        val url = GitHubAuthBiz().getAuthUrl(authParam)
        println("auth url: $url")
    }

    @Test
    fun getUserInfo() {
        val authParam = AuthParam.Builder()
            .clientId(CLIENT_ID)
            .clientSecret(CLIENT_SECRET)
            .code("462b3f1ba6977788dc51") // 需要先访问getAuthUrl()生成的url，登录授权后，会返回code。code只能使用一次
            .build()
        val gitHubAuthBiz = GitHubAuthBiz()
        val token = gitHubAuthBiz.getToken(authParam)
        val userInfo = gitHubAuthBiz.getUserInfo(token!!.accessToken)
        println(userInfo)
    }

}