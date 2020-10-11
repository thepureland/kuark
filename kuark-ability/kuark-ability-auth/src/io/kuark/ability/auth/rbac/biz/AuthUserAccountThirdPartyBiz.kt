package io.kuark.ability.auth.rbac.biz

import io.kuark.ability.data.rdb.support.RdbKit
import io.kuark.ability.auth.rbac.dao.AuthUserAccountThirdPartys
import io.kuark.ability.auth.rbac.ibiz.IAuthUserAccountThirdPartyBiz
import io.kuark.ability.auth.rbac.po.AuthUserAccountThirdParty
import org.ktorm.dsl.*
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
import org.springframework.stereotype.Service

/**
 * 用户账号第三方授权业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
class AuthUserAccountThirdPartyBiz : IAuthUserAccountThirdPartyBiz {
//endregion your codes 1

    //region your codes 2

    override fun isIdentifierExists(
        identityTypeDictCode: String,
        identifier: String,
        subSysDictCode: String?,
        ownerId: String?
    ): Boolean =
        RdbKit.getDatabase().from(AuthUserAccountThirdPartys)
            .select(AuthUserAccountThirdPartys.id)
            .whereWithConditions {
                it += (AuthUserAccountThirdPartys.identifier eq identifier) and
                        (AuthUserAccountThirdPartys.identityTypeDictCode eq identityTypeDictCode) and
                        (AuthUserAccountThirdPartys.isActive eq true)
                if (subSysDictCode != null && subSysDictCode.isNotBlank()) {
                    it += (AuthUserAccountThirdPartys.subSysDictCode eq subSysDictCode)
                }
                if (ownerId != null && ownerId.isNotBlank()) {
                    it += (AuthUserAccountThirdPartys.ownerId eq ownerId)
                }
            }
            .totalRecords != 0

    override fun save(userAccountThirdParty: AuthUserAccountThirdParty): Boolean {
        return RdbKit.getDatabase().sequenceOf(AuthUserAccountThirdPartys).add(userAccountThirdParty) == 1
    }

    //endregion your codes 2

}