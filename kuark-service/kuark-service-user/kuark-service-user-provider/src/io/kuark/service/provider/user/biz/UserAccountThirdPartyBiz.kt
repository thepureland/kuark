package io.kuark.service.provider.user.biz

import io.kuark.ability.data.rdb.support.RdbKit
import io.kuark.service.user.dao.UserAccountThirdPartys
import io.kuark.service.user.ibiz.IUserAccountThirdPartyBiz
import io.kuark.service.user.po.UserAccountThirdParty
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.sequenceOf
import org.springframework.stereotype.Service

/**
 * 用户账号第三方授权业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
class UserAccountThirdPartyBiz : IUserAccountThirdPartyBiz {
//endregion your codes 1

    //region your codes 2

    override fun isIdentifierExists(
        identityTypeDictCode: String,
        identifier: String,
        subSysDictCode: String?,
        ownerId: String?
    ): Boolean =
        RdbKit.getDatabase().from(UserAccountThirdPartys)
            .select(UserAccountThirdPartys.id)
            .whereWithConditions {
                it += (UserAccountThirdPartys.identifier eq identifier) and
                        (UserAccountThirdPartys.identityTypeDictCode eq identityTypeDictCode) and
                        (UserAccountThirdPartys.isActive eq true)
                if (subSysDictCode != null && subSysDictCode.isNotBlank()) {
                    it += (UserAccountThirdPartys.subSysDictCode eq subSysDictCode)
                }
                if (ownerId != null && ownerId.isNotBlank()) {
                    it += (UserAccountThirdPartys.ownerId eq ownerId)
                }
            }
            .totalRecords != 0

    override fun save(userAccountThirdParty: UserAccountThirdParty): Boolean {
        return RdbKit.getDatabase().sequenceOf(UserAccountThirdPartys).add(userAccountThirdParty) == 1
    }

    //endregion your codes 2

}