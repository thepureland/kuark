package io.kuark.ability.auth.third.github

import io.kuark.ability.auth.third.core.AuthParam
import io.kuark.ability.auth.third.core.IAuthToken
import io.kuark.ability.auth.third.core.IAuthUser
import io.kuark.ability.auth.third.core.IAuthBiz
import io.kuark.base.data.json.JsonKit
import io.kuark.base.lang.string.RandomStringKit
import io.kuark.base.net.http.HttpClientKit
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.text.MessageFormat
import java.time.Duration


/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Service
open class GitHubAuthBiz : IAuthBiz {

    companion object {
        private const val AUTH_URL_PATTERN =
            "https://github.com/login/oauth/authorize?client_id={0}&redirect_uri={1}&state={2}"
        private const val GET_TOKEN_URL = "https://github.com/login/oauth/access_token"
        private const val GET_USER_URL = "https://api.github.com/user?access_token={0}"
    }

    override fun getAuthUrl(authParam: AuthParam): String {
        val state = RandomStringKit.randomAlphanumeric(10)
        return MessageFormat.format(AUTH_URL_PATTERN, authParam.clientId, authParam.redirectUri, state)
    }

    override fun getToken(authParam: AuthParam): IAuthToken? {
        val params = mapOf(
            "client_id" to authParam.clientId,
            "client_secret" to authParam.clientSecret,
            "code" to authParam.code,
            "redirect_uri" to authParam.redirectUri,
            "state" to authParam.state
        )

        val httpClientBuilder = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30))
        val httpRequestBuilder = HttpRequest.newBuilder()
            .uri(URI(GET_TOKEN_URL))
            .timeout(Duration.ofSeconds(30))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(JsonKit.toJson(params)))
        val response =
            HttpClientKit.asyncRequest(httpClientBuilder, httpRequestBuilder, HttpResponse.BodyHandlers.ofString())
        val resultJson = response.body()
        return JsonKit.fromJson(resultJson, GithubAuthToken::class)
    }

    override fun getUserInfo(token: String): IAuthUser? {
        val httpClientBuilder = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30))
        val url = MessageFormat.format(GET_USER_URL, token)
        val httpRequestBuilder = HttpRequest.newBuilder()
            .uri(URI(url))
            .timeout(Duration.ofSeconds(30))
            .header("Accept", "application/json")
            .GET()
        val response =
            HttpClientKit.asyncRequest(httpClientBuilder, httpRequestBuilder, HttpResponse.BodyHandlers.ofString())
        val resultJson = response.body()
        return JsonKit.fromJson(resultJson, GithubAuthUser::class)
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