package io.kuark.ability.auth.login.third.github

import io.kuark.ability.auth.login.third.core.AuthParam
import io.kuark.ability.auth.login.third.core.IAuthToken
import io.kuark.ability.auth.login.third.core.IAuthUser
import io.kuark.ability.auth.login.third.core.IAuthBiz
import io.kuark.base.data.json.JsonKit
import io.kuark.base.lang.string.RandomStringKit
import io.kuark.base.net.http.HttpClientKit
import org.springframework.beans.factory.annotation.Value
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

    @Value("\${ability.auth.github.clientId:620413e00b95a0e63eda}")
    private lateinit var clientId: String

    @Value("\${ability.auth.github.clientSecret:a64e7e0ac14f86802c8be1663cabbbc620affc38}")
    private lateinit var clientSecret: String

    @Value("\${ability.auth.github.redirectUri:http://localhost:8080/oauth/github/callback}")
    private lateinit var redirectUri: String


    override fun getAuthUrl(authParamBuilder: AuthParam.Builder): String {
        val authParam = authParamBuilder.clientId(clientId).redirectUri(redirectUri).build()
        val state = RandomStringKit.randomAlphanumeric(10)
        return MessageFormat.format(AUTH_URL_PATTERN, authParam.clientId, authParam.redirectUri, state)
    }

    override fun getToken(authParamBuilder: AuthParam.Builder): IAuthToken? {
        val authParam = authParamBuilder.clientId(clientId).clientSecret(clientSecret).build()
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