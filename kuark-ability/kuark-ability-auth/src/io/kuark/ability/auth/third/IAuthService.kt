package io.kuark.ability.auth.third

interface IAuthService {

    fun getToken(params: Map<String, String>): String

    fun getUserInfo(token: String?): AuthUserVO?

    fun checkIsExistsOpenId(openId: String): Boolean

    fun storeOpenIdByUser(openId: String, userId: Int?): Boolean

    fun getUserNameByOpenId(openId: String): String?

}