package io.kuark.service.user.provider.login.third.core

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
interface IThirdPartyAuthBizFactory {

    fun getAuthBiz(type: String): IAuthBiz

}