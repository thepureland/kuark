package io.kuark.service.user.provider.login.third.github

import io.kuark.service.auth.provider.login.third.core.AuthParam
import org.junit.jupiter.api.Test

internal class GitHubAuthBizTest {

    @Test
    fun getAuthUrl() {
        val authParamBuilder = AuthParam.Builder()
        val url = GitHubAuthBiz().getAuthUrl(authParamBuilder)
        println("auth url: $url")
    }

    @Test
    fun getUserInfo() {
        val authParamBuilder = AuthParam.Builder()
            .code("f157a6b2fc4b023c4c6c") // 需要先访问getAuthUrl()生成的url，登录授权后，会返回code。code只能使用一次
        val gitHubAuthBiz = GitHubAuthBiz()
        val token = gitHubAuthBiz.getToken(authParamBuilder)
        val userInfo = gitHubAuthBiz.getUserInfo(token!!.accessToken)
        println(userInfo)
    }

}