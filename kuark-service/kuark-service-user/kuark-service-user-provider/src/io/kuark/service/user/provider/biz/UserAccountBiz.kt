package io.kuark.service.user.provider.biz

import io.kuark.ability.data.rdb.support.RdbKit
import io.kuark.service.user.provider.dao.UserAccounts
import io.kuark.service.user.provider.ibiz.IUserAccountBiz
import io.kuark.service.user.provider.po.UserAccount
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
class UserAccountBiz : IUserAccountBiz {
//endregion your codes 1

    //region your codes 2

    override fun isUsernameExists(username: String, subSysDictCode: String?, ownerId: String?): Boolean =
        RdbKit.getDatabase().from(UserAccounts)
            .select(UserAccounts.username)
            .whereWithConditions {
                it += (UserAccounts.username eq username)
                if (subSysDictCode != null && subSysDictCode.isNotBlank()) {
                    it += (UserAccounts.subSysDictCode eq subSysDictCode)
                }
                if (ownerId != null && ownerId.isNotBlank()) {
                    it += (UserAccounts.ownerId eq ownerId)
                }
            }
            .totalRecords != 0

    override fun register(userAccount: UserAccount): Boolean =
        RdbKit.getDatabase().sequenceOf(UserAccounts).add(userAccount) == 1


    //endregion your codes 2

}