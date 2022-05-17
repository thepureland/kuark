package io.kuark.service.user.provider.user.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.resource.MenuTreeNode
import io.kuark.service.user.common.user.vo.account.UserAccountCacheItem
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

    fun isUsernameExists(subSysDictCode: String, username: String): Boolean

    fun register(userAccount: UserAccount): Boolean

    fun getByUserId(userId: String): UserAccountCacheItem?

    fun getByUsername(subSysDictCode: String, username: String): UserAccountCacheItem?

    fun getPermissions(userId: String): Set<String>

    fun getMenuPermissions(userId: String): Set<String>

    fun getAuthorisedMenus(userId: String): List<MenuTreeNode>

    fun getAccounts(subSysDictCode: String, tenantId: String?): List<UserAccountRecord>

    //endregion your codes 2

}