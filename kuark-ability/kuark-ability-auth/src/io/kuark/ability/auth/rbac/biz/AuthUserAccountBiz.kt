package io.kuark.ability.auth.rbac.biz

import io.kuark.ability.auth.rbac.dao.AuthUserAccountDao
import io.kuark.ability.auth.rbac.model.table.AuthUserAccounts
import io.kuark.ability.auth.rbac.ibiz.IAuthUserAccountBiz
import io.kuark.ability.auth.rbac.model.po.AuthUserAccount
import io.kuark.context.core.KuarkContextHolder
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 用户账号业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
class AuthUserAccountBiz : IAuthUserAccountBiz {
//endregion your codes 1

    @Autowired
    private lateinit var authUserAccountDao: AuthUserAccountDao

    private lateinit var authUserGroupUserDao: AuthUserGroupUserDao

    //region your codes 2

    override fun isUsernameExists(username: String): Boolean {
        val context = KuarkContextHolder.get()
        val subSysDictCode = context.subSysCode
        val ownerId = context.ownerId

        return authUserAccountDao.querySource()
            .select(AuthUserAccounts.username)
            .whereWithConditions {
                it += (AuthUserAccounts.username eq username)
                if (subSysDictCode != null && subSysDictCode.isNotBlank()) {
                    it += (AuthUserAccounts.subSysDictCode eq subSysDictCode)
                }
                if (ownerId != null && ownerId.isNotBlank()) {
                    it += (AuthUserAccounts.ownerId eq ownerId)
                }
            }
            .totalRecords != 0
    }

    override fun register(userAccount: AuthUserAccount): Boolean =
        authUserAccountDao.entitySequence().add(userAccount) == 1

    override fun getByUsername(username: String): AuthUserAccount? {
        val context = KuarkContextHolder.get()
        val subSysDictCode = context.subSysCode
        val ownerId = context.ownerId


        val userAccounts = authUserAccountDao.querySource()
            .select()
            .whereWithConditions {
                it += (AuthUserAccounts.username eq username)
                if (subSysDictCode != null && subSysDictCode.isNotBlank()) {
                    it += (AuthUserAccounts.subSysDictCode eq subSysDictCode)
                }
                if (ownerId != null && ownerId.isNotBlank()) {
                    it += (AuthUserAccounts.ownerId eq ownerId)
                }
            }.map { row -> AuthUserAccounts.createEntity(row) }

        return if (userAccounts.isEmpty()) {
            null
        } else userAccounts.first()
    }

    override fun getPermissions(userId: String): Set<String> {
        // 得到用户隶属的用户组


        // 得到用户组关联的角色


        // 得到用户所关联的角色

        // 合并所有关联的角色，并得到这些角色的权限



        TODO("Not yet implemented")
    }


    //endregion your codes 2

}