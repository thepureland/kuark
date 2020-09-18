package io.kuark.service.user.biz

import io.kuark.ability.data.jdbc.support.RdbKit
import io.kuark.service.user.dao.UserAccountThirdPartyAuths
import io.kuark.service.user.ibiz.IUserAccountThirdPartyAuthBiz
import me.liuwj.ktorm.dsl.*
import org.springframework.stereotype.Service

/**
 * 用户账号授权业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
class UserAccountThirdPartyAuthBiz : IUserAccountThirdPartyAuthBiz {
//endregion your codes 1

    //region your codes 2

    override fun isIdentifierExists(
        identityTypeDictCode: String,
        identifier: String,
        subSysDictCode: String?,
        ownerId: String?
    ): Boolean {
        return RdbKit.getDatabase().from(UserAccountThirdPartyAuths)
            .select(UserAccountThirdPartyAuths.id)
            .whereWithConditions {
                it += (UserAccountThirdPartyAuths.identifier eq identifier) and
                        (UserAccountThirdPartyAuths.identityTypeDictCode eq identityTypeDictCode) and
                        (UserAccountThirdPartyAuths.isActive eq true)
                if (subSysDictCode != null && subSysDictCode.isNotBlank()) {
                    it += (UserAccountThirdPartyAuths.subSysDictCode eq subSysDictCode)
                }
                if (ownerId != null && ownerId.isNotBlank()) {
                    it += (UserAccountThirdPartyAuths.ownerId eq ownerId)
                }
            }
            .map { row -> row[UserAccountThirdPartyAuths.id] }
            .isNotEmpty()
    }

    //endregion your codes 2

}