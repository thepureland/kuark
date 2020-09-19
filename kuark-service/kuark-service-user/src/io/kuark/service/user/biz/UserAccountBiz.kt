package io.kuark.service.user.biz

import io.kuark.ability.data.jdbc.support.RdbKit
import io.kuark.service.user.dao.UserAccounts
import io.kuark.service.user.ibiz.IUserAccountBiz
import io.kuark.service.user.po.UserAccount
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.sequenceOf
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