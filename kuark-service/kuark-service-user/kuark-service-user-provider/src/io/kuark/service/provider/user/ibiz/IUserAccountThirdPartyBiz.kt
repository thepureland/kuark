package io.kuark.service.provider.user.ibiz

import io.kuark.service.user.po.UserAccountThirdParty

/**
 * 用户账号第三方授权业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IUserAccountThirdPartyBiz {
//endregion your codes 1

    //region your codes 2

    fun isIdentifierExists(
        identityTypeDictCode: String,
        identifier: String,
        subSysDictCode: String? = null,
        ownerId: String? = null
    ): Boolean

    fun save(userAccountThirdParty: UserAccountThirdParty): Boolean

    //endregion your codes 2

}