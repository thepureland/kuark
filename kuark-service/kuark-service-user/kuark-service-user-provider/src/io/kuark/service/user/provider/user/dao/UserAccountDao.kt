package io.kuark.service.user.provider.user.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.user.provider.user.model.po.UserAccount
import io.kuark.service.user.provider.user.model.table.UserAccounts
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
open class UserAccountDao : BaseCrudDao<String, UserAccount, UserAccounts>() {
//endregion your codes 1

    //region your codes 2

//    fun isUsernameExists(subSysDictCode: String, username: String): Boolean {
//        val context = KuarkContextHolder.get()
//        val subSysDictCode = context.subSysCode
//        val tenantId = context.tenantId
//
//        return querySource()
//            .select(UserAccounts.username)
//            .whereWithConditions {
//                it += (UserAccounts.username eq username)
//                if (StringKit.isNotBlank(subSysDictCode)) {
//                    it += (UserAccounts.subSysDictCode eq subSysDictCode!!)
//                }
//                if (StringKit.isNotBlank(tenantId)) {
//                    it += (UserAccounts.tenantId eq tenantId!!)
//                }
//            }
//            .totalRecords != 0
//    }
//
//    fun getByUsername(username: String): UserAccount? {
//        val context = KuarkContextHolder.get()
//        val subSysDictCode = context.subSysCode
//        val tenantId = context.tenantId
//
//
//        val userAccounts = querySource()
//            .select()
//            .whereWithConditions {
//                it += (UserAccounts.username eq username)
//                if (StringKit.isNotBlank(subSysDictCode)) {
//                    it += (UserAccounts.subSysDictCode eq subSysDictCode!!)
//                }
//                if (StringKit.isNotBlank(tenantId)) {
//                    it += (UserAccounts.tenantId eq tenantId!!)
//                }
//            }.map { row -> UserAccounts.createEntity(row) }
//
//        return if (userAccounts.isEmpty()) {
//            null
//        } else userAccounts.first()
//    }

    fun register(userAccount: UserAccount): Boolean = entitySequence().add(userAccount) == 1

    //endregion your codes 2

}