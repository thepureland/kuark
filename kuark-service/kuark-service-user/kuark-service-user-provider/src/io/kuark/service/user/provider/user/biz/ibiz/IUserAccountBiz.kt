package io.kuark.service.user.provider.user.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.MenuTreeNode
import io.kuark.service.user.common.user.vo.account.UserAccountCacheItem
import io.kuark.service.user.common.user.vo.account.UserAccountRecord
import io.kuark.service.user.provider.user.model.po.UserAccount
import kotlin.reflect.KClass


/**
 * 用户账号业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IUserAccountBiz : IBaseCrudBiz<String, UserAccount> {
//endregion your codes 1

    //region your codes 2

    /**
     * 检测指定子系统和用户名的用户是否存在
     *
     * @param subSysDictCode 子系统代码
     * @param username 用户名，大小写不敏感
     * @return true: 存在，false: 不存在
     * @author K
     * @since 1.0.0
     */
    fun isUsernameExists(subSysDictCode: String, username: String): Boolean

    fun register(userAccount: UserAccount): Boolean

    fun getByUserId(userId: String): UserAccountCacheItem?

    /**
     * 返回指定子系统和用户名的用户
     *
     * @param subSysDictCode 子系统代码
     * @param username 用户名，大小写不敏感
     * @return 用户账号缓存对象，不存在返回null
     * @author K
     * @since 1.0.0
     */
    fun getByUsername(subSysDictCode: String, username: String): UserAccountCacheItem?

    fun getPermissions(userId: String): Set<String>

    fun getMenuPermissions(userId: String): Set<String>

    fun <T : BaseMenuTreeNode> getAuthorisedMenus(userId: String, clazz: KClass<T>): List<T>

    fun getAccounts(subSysDictCode: String, tenantId: String?): List<UserAccountRecord>

    //endregion your codes 2

}