package io.kuark.ability.auth.third.core

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
interface IThirdPartyAuthBizFactory {

    fun getAuthBiz(type: String): IAuthBiz

}