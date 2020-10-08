package io.kuark.service.user.provider.ibiz

import io.kuark.service.user.provider.po.UserAccount

/**
 * 用户账号业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IUserAccountBiz {
//endregion your codes 1

    //region your codes 2

    fun isUsernameExists(username: String, subSysDictCode: String? = null, ownerId: String? = null): Boolean

    fun register(userAccount: UserAccount): Boolean

    //endregion your codes 2

}