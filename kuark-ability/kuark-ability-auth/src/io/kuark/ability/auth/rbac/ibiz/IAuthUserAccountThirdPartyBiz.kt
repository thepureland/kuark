package io.kuark.ability.auth.rbac.ibiz

import io.kuark.ability.auth.rbac.po.AuthUserAccountThirdParty

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

    fun isIdentifierExists(
        identityTypeDictCode: String,
        identifier: String,
        subSysDictCode: String? = null,
        ownerId: String? = null
    ): Boolean

    fun save(userAccountThirdParty: AuthUserAccountThirdParty): Boolean

    //endregion your codes 2

}