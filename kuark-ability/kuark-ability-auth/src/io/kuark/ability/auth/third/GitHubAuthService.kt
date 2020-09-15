package io.kuark.ability.auth.third

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


/**
 *
 *
 * @author K
 * @since 1.0.0
 */
//@Service
//open class GitHubAuthService : IAuthService {
//
//    @Autowired
//    private val gitHubMapper: AuthForGitHubMapper? = null
//
//    override fun getToken(params: MultiValueMap<String?, String?>): String {
//        params.add("client_id", CLIENT_ID)
//        params.add("client_secret", CLIENT_SECRET)
//        params.add("redirect_uri", REDIRECT_URI)
//        val authTokenVO: AuthTokenVO = AuthHelper.sendPostGetToken(GET_TOKEN_URL, params)
//        return authTokenVO.getAccess_token()
//    }
//
//    fun getUserInfo(token: String): AuthUserVO {
//        val map: MutableMap<String, String> = HashMap()
//        map["access_token"] = token
//        return AuthHelper.sendGetToUser(GET_USER_URL, map)
//    }
//
//    fun checkIsExistsOpenId(openId: String?): Boolean {
//        return gitHubMapper.checkIsExists(openId) > 0
//    }
//
//    fun storeOpenIdByUser(openId: String?, userId: Int?): Boolean {
//        val date = Date()
//        val timeStamp = Timestamp(date.getTime())
//        return gitHubMapper.storeOpenIdByUser(openId, userId, timeStamp) > 0
//    }
//
//    fun getUserNameByOpenId(openId: String?): String {
//        return gitHubMapper.getUserNameByOpenId(openId)
//    }
//
//    companion object {
//        private const val GET_TOKEN_URL = "https://github.com/login/oauth/access_token"
//        private const val GET_USER_URL = "https://api.github.com/user"
//        private const val CLIENT_ID = "50d7f61132da7f8574a1"
//        private const val CLIENT_SECRET = "6779d154cfc44115e1f3607c0000085c5c1cf178"
//        private const val REDIRECT_URI = "http://localhost:8080/oauth/github/callback"
//    }
//
//}