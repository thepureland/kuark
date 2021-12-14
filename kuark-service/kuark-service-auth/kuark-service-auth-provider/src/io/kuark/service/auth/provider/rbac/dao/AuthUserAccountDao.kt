package io.kuark.service.auth.provider.rbac.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.base.lang.string.StringKit
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.auth.provider.rbac.model.po.AuthUserAccount
import io.kuark.service.auth.provider.rbac.model.table.AuthUserAccounts
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.whereWithConditions
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
open class AuthUserAccountDao : BaseCrudDao<String, AuthUserAccount, AuthUserAccounts>() {
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