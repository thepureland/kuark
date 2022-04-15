package io.kuark.service.user.provider.user.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.user.common.user.vo.account.UserAccountRecord
import io.kuark.service.user.provider.user.model.po.UserAccount


/**
 * 用户账号业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IUserAccountBiz: IBaseCrudBiz<String, UserAccount> {
//endregion your codes 1

    //region your codes 2

    fun isUsernameExists(username: String): Boolean

    fun register(userAccount: UserAccount): Boolean

    fun getByUsername(username: String): UserAccount?

    fun getMenuPermissions(userId: String): Set<String>

    fun getAccounts(subSysDictCode: String, tenantId: String?): List<UserAccountRecord>

    //endregion your codes 2

}