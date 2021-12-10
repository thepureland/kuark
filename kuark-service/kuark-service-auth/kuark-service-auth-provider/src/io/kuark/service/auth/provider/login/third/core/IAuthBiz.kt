package io.kuark.service.auth.provider.login.third.core

interface IAuthBiz {

    fun getAuthUrl(authParamBuilder: AuthParam.Builder): String

    fun getToken(authParamBuilder: AuthParam.Builder): IAuthToken?

    fun getUserInfo(token: String): IAuthUser?

    fun checkIsExistsOpenId(openId: String): Boolean

    fun storeOpenIdByUser(openId: String, userId: Int?): Boolean

    fun getUserNameByOpenId(openId: String): String?

}