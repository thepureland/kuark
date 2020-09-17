package io.kuark.ability.auth.third.core

interface IAuthBiz {

    fun getAuthUrl(authParam: AuthParam): String

    fun getToken(authParam: AuthParam): IAuthToken?

    fun getUserInfo(token: String): IAuthUser?

    fun checkIsExistsOpenId(openId: String): Boolean

    fun storeOpenIdByUser(openId: String, userId: Int?): Boolean

    fun getUserNameByOpenId(openId: String): String?

}