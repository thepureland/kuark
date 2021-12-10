package io.kuark.service.auth.provider.rbac.ibiz

import io.kuark.service.auth.provider.login.general.PrincipalType
import io.kuark.service.auth.provider.rbac.model.po.AuthUserAccountThirdParty

/**
 * 用户账号第三方授权业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IAuthUserAccountThirdPartyBiz {
//endregion your codes 1

    //region your codes 2

    fun isIdentifierExists(identityTypeDictCode: String, identifier: String): Boolean

    fun save(userAccountThirdParty: AuthUserAccountThirdParty): Boolean

    fun getByPrincipal(principalType: PrincipalType, principal: String): AuthUserAccountThirdParty

    //endregion your codes 2

}