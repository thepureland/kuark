package io.kuark.ability.auth.rbac.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.ability.auth.rbac.model.po.AuthUserAccount
import io.kuark.ability.auth.rbac.model.table.AuthUserAccounts
import io.kuark.base.lang.string.StringKit
import io.kuark.context.core.KuarkContextHolder
import org.ktorm.dsl.*
import org.ktorm.entity.add
import org.springframework.stereotype.Repository

/**
 * 用户账号数据访问对象
 *
 * @author hanfei
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class AuthUserAccountDao : BaseDao<String, AuthUserAccount, AuthUserAccounts>() {
//endregion your codes 1

    //region your codes 2

    fun isUsernameExists(username: String): Boolean {
        val context = KuarkContextHolder.get()
        val subSysDictCode = context.subSysCode
        val ownerId = context.ownerId

        return querySource()
            .select(AuthUserAccounts.username)
            .whereWithConditions {
                it += (AuthUserAccounts.username eq username)
                if (StringKit.isNotBlank(subSysDictCode)) {
                    it += (AuthUserAccounts.subSysDictCode eq subSysDictCode!!)
                }
                if (StringKit.isNotBlank(ownerId)) {
                    it += (AuthUserAccounts.ownerId eq ownerId!!)
                }
            }
            .totalRecords != 0
    }

    fun getByUsername(username: String): AuthUserAccount? {
        val context = KuarkContextHolder.get()
        val subSysDictCode = context.subSysCode
        val ownerId = context.ownerId


        val userAccounts = querySource()
            .select()
            .whereWithConditions {
                it += (AuthUserAccounts.username eq username)
                if (StringKit.isNotBlank(subSysDictCode)) {
                    it += (AuthUserAccounts.subSysDictCode eq subSysDictCode!!)
                }
                if (StringKit.isNotBlank(ownerId)) {
                    it += (AuthUserAccounts.ownerId eq ownerId!!)
                }
            }.map { row -> AuthUserAccounts.createEntity(row) }

        return if (userAccounts.isEmpty()) {
            null
        } else userAccounts.first()
    }

    fun register(userAccount: AuthUserAccount): Boolean = entitySequence().add(userAccount) == 1

    //endregion your codes 2

}