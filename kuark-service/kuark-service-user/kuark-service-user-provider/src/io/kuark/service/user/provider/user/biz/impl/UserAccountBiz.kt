package io.kuark.service.user.provider.user.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.query.sort.Order
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.api.ISysTenantApi
import io.kuark.service.user.common.rbac.vo.role.RbacRoleDetail
import io.kuark.service.user.common.user.vo.account.UserAccountCacheItem
import io.kuark.service.user.common.user.vo.account.UserAccountDetail
import io.kuark.service.user.common.user.vo.account.UserAccountRecord
import io.kuark.service.user.common.user.vo.account.UserAccountSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import io.kuark.service.user.provider.rbac.dao.RbacRoleUserDao
import io.kuark.service.user.provider.rbac.model.po.RbacRoleUser
import io.kuark.service.user.provider.user.biz.ibiz.IUserAccountBiz
import io.kuark.service.user.provider.user.cache.UserByIdCacheHandler
import io.kuark.service.user.provider.user.cache.UserIdBySubSysAndUsernameCacheHandler
import io.kuark.service.user.provider.user.dao.UserAccountDao
import io.kuark.service.user.provider.user.model.po.UserAccount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KClass

/**
 * 用户账号业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class UserAccountBiz : BaseCrudBiz<String, UserAccount, UserAccountDao>(), IUserAccountBiz {
//endregion your codes 1


    //region your codes 2

    @Autowired
    private lateinit var rbacRoleUserDao: RbacRoleUserDao

    @Autowired
    private lateinit var rbacRoleBiz: IRbacRoleBiz

    @Autowired
    protected lateinit var userByIdCacheHandler: UserByIdCacheHandler

    @Autowired
    protected lateinit var userIdByUsernameCacheHandler: UserIdBySubSysAndUsernameCacheHandler


    @Autowired
    private lateinit var sysTenantApi: ISysTenantApi

    override fun <R : Any> get(id: String, returnType: KClass<R>): R? {
        val result = super.get(id, returnType)
        if (returnType == UserAccountDetail::class) {
            val tenantId = (result as UserAccountDetail).tenantId
            result.tenantName = sysTenantApi.getTenant(tenantId!!)?.name
        }
        return result
    }

    override fun isUsernameExists(subSysDictCode: String, username: String): Boolean {
        val userId = userIdByUsernameCacheHandler.getUserId(subSysDictCode, username)
        return userId != null
    }

    @Transactional
    override fun register(userAccount: UserAccount): Boolean = dao.register(userAccount)

    override fun getByUserId(userId: String): UserAccountCacheItem? {
        return userByIdCacheHandler.getUserById(userId)
    }

    override fun getByUsername(subSysDictCode: String, username: String): UserAccountCacheItem? {
        val userId = userIdByUsernameCacheHandler.getUserId(subSysDictCode, username) ?: return null
        return userByIdCacheHandler.getUserById(userId)
    }

    override fun getMenuPermissions(userId: String): Set<String> {
        // 得到用户隶属的用户组
        //TODO

        // 得到用户组关联的角色
        //TODO

        // 得到用户所关联的角色
        val roleIds = rbacRoleUserDao.oneSearchProperty(RbacRoleUser::userId.name, userId, RbacRoleUser::roleId.name)

        // 合并所有关联的角色，并得到这些角色的权限
        val resIds = mutableSetOf<String>()
        roleIds.forEach { roleId ->
            val menuPermissions = rbacRoleBiz.getMenuPermissions(roleId!! as String)
            resIds.addAll(menuPermissions.second)
        }
        return resIds
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun getAccounts(subSysDictCode: String, tenantId: String?): List<UserAccountRecord> {
        val searchPayload = UserAccountSearchPayload()
        searchPayload.subSysDictCode = subSysDictCode
        searchPayload.tenantId = tenantId
        searchPayload.orders = listOf(Order.asc(UserAccount::username.name))
        return dao.search(searchPayload) as List<UserAccountRecord>
    }

    //endregion your codes 2

}