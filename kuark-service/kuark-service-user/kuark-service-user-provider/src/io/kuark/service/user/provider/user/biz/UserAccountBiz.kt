package io.kuark.service.user.provider.user.biz

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.service.user.provider.user.dao.UserAccountDao
import io.kuark.service.user.provider.user.ibiz.IUserAccountBiz
import io.kuark.service.user.provider.user.model.po.UserAccount
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    override fun isUsernameExists(username: String): Boolean = dao.isUsernameExists(username)

    @Transactional
    override fun register(userAccount: UserAccount): Boolean = dao.register(userAccount)

    override fun getByUsername(username: String): UserAccount? = dao.getByUsername(username)

    override fun getPermissions(userId: String): Set<String> {
        // 得到用户隶属的用户组


        // 得到用户组关联的角色


        // 得到用户所关联的角色

        // 合并所有关联的角色，并得到这些角色的权限



        TODO("Not yet implemented")
    }

    override fun pagingSearch(listSearchPayload: ListSearchPayload): Pair<List<*>, Int> {

        return super.pagingSearch(listSearchPayload)
    }


    //endregion your codes 2

}