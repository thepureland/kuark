package io.kuark.ability.auth.rbac.biz

import io.kuark.ability.auth.login.general.PrincipalType
import io.kuark.ability.auth.rbac.dao.AuthUserAccountThirdPartyDao
import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.ability.auth.rbac.model.table.AuthUserAccountThirdPartys
import io.kuark.ability.auth.rbac.ibiz.IAuthUserAccountThirdPartyBiz
import io.kuark.ability.auth.rbac.model.po.AuthUserAccountThirdParty
import io.kuark.context.core.KuarkContextHolder
import org.ktorm.dsl.*
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    private lateinit var authUserAccountThirdPartyDao: AuthUserAccountThirdPartyDao

    //region your codes 2

    override fun isIdentifierExists(identityTypeDictCode: String, identifier: String): Boolean {
        val context = KuarkContextHolder.get()
        val subSysDictCode = context.subSysCode
        val ownerId = context.ownerId

        return authUserAccountThirdPartyDao.querySource()
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
    }

    override fun save(userAccountThirdParty: AuthUserAccountThirdParty): Boolean {
        return RdbKit.getDatabase().sequenceOf(AuthUserAccountThirdPartys).add(userAccountThirdParty) == 1
    }

    override fun getByPrincipal(principalType: PrincipalType, principal: String): AuthUserAccountThirdParty {
        TODO("Not yet implemented")
    }

    //endregion your codes 2

}