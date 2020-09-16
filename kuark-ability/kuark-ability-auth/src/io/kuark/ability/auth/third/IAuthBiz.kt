package io.kuark.ability.auth.third

interface IAuthBiz {

    fun getAuthUrl(authParam: AuthParam): String

    fun getToken(authParam: AuthParam): String

    fun getUserInfo(token: String): AuthUserVO?

    fun checkIsExistsOpenId(openId: String): Boolean

    fun storeOpenIdByUser(openId: String, userId: Int?): Boolean

    fun getUserNameByOpenId(openId: String): String?

}