package io.kuark.service.user.provider.user.biz.ibiz

import io.kuark.service.user.provider.login.general.PrincipalType
import io.kuark.service.user.provider.user.model.po.UserAccountThird

/**
 * 用户账号第三方授权业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IUserAccountThirdBiz {
//endregion your codes 1

    //region your codes 2

    fun isIdentifierExists(identityTypeDictCode: String, identifier: String): Boolean

    fun save(userAccountThirdParty: UserAccountThird): Boolean

    fun getByPrincipal(principalType: PrincipalType, principal: String): UserAccountThird

    //endregion your codes 2

}