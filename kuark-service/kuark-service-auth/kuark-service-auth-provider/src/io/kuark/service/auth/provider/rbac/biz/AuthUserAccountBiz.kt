package io.kuark.service.auth.provider.rbac.biz

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.service.auth.provider.rbac.dao.AuthUserAccountDao
import io.kuark.service.auth.provider.rbac.ibiz.IAuthUserAccountBiz
import io.kuark.service.auth.provider.rbac.model.po.AuthUserAccount
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
open class AuthUserAccountBiz : BaseCrudBiz<String, AuthUserAccount, AuthUserAccountDao>(), IAuthUserAccountBiz {
//endregion your codes 1


    //region your codes 2

    override fun isUsernameExists(username: String): Boolean = dao.isUsernameExists(username)

    @Transactional
    override fun register(userAccount: AuthUserAccount): Boolean = dao.register(userAccount)

    override fun getByUsername(username: String): AuthUserAccount? = dao.getByUsername(username)

    override fun getPermissions(userId: String): Set<String> {
        // 得到用户隶属的用户组


        // 得到用户组关联的角色


        // 得到用户所关联的角色

        // 合并所有关联的角色，并得到这些角色的权限



        TODO("Not yet implemented")
    }


    //endregion your codes 2

}