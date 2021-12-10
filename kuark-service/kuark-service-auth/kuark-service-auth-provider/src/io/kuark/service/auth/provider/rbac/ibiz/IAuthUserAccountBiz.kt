package io.kuark.service.auth.provider.rbac.ibiz

import io.kuark.service.auth.provider.rbac.model.po.AuthUserAccount


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

    fun isUsernameExists(username: String): Boolean

    fun register(userAccount: AuthUserAccount): Boolean

    fun getByUsername(username: String): AuthUserAccount?

    fun getPermissions(userId: String): Set<String>

    //endregion your codes 2

}