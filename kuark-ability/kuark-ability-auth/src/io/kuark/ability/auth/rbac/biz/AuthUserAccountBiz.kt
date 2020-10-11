package io.kuark.ability.auth.rbac.biz

import io.kuark.ability.data.rdb.support.RdbKit
import io.kuark.ability.auth.rbac.dao.AuthUserAccounts
import io.kuark.ability.auth.rbac.ibiz.IAuthUserAccountBiz
import io.kuark.ability.auth.rbac.po.AuthUserAccount
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.dsl.whereWithConditions
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
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

    //region your codes 2

    override fun isUsernameExists(username: String, subSysDictCode: String?, ownerId: String?): Boolean =
        RdbKit.getDatabase().from(AuthUserAccounts)
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

    override fun register(userAccount: AuthUserAccount): Boolean =
        RdbKit.getDatabase().sequenceOf(AuthUserAccounts).add(userAccount) == 1


    //endregion your codes 2

}