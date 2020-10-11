package io.kuark.ability.auth.rbac.ibiz

import io.kuark.ability.auth.rbac.po.AuthUserAccount

/**
 * 用户账号业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IAuthUserAccountBiz {
//endregion your codes 1

    //region your codes 2

    fun isUsernameExists(username: String, subSysDictCode: String? = null, ownerId: String? = null): Boolean

    fun register(userAccount: AuthUserAccount): Boolean

    //endregion your codes 2

}