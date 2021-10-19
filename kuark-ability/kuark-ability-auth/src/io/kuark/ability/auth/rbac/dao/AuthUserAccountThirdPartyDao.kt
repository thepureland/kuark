package io.kuark.ability.auth.rbac.dao

import io.kuark.ability.auth.rbac.model.po.AuthUserAccountThirdParty
import io.kuark.ability.auth.rbac.model.table.AuthUserAccountThirdPartys
import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.base.lang.string.StringKit
import io.kuark.context.core.KuarkContextHolder
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.select
import org.ktorm.dsl.whereWithConditions
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
import org.springframework.stereotype.Repository

/**
 * 用户账号第三方授权信息数据访问对象
 *
 * @author hanfei
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class AuthUserAccountThirdPartyDao : BaseDao<String, AuthUserAccountThirdParty, AuthUserAccountThirdPartys>() {
//endregion your codes 1

    //region your codes 2

    fun isIdentifierExists(identityTypeDictCode: String, identifier: String): Boolean {
        val context = KuarkContextHolder.get()
        val subSysDictCode = context.subSysCode
        val ownerId = context.ownerId

        return querySource()
            .select(AuthUserAccountThirdPartys.id)
            .whereWithConditions {
                it += (AuthUserAccountThirdPartys.principal eq identifier) and
                        (AuthUserAccountThirdPartys.principalTypeDictCode eq identityTypeDictCode) and
                        (AuthUserAccountThirdPartys.active eq true)
                if (StringKit.isNotBlank(subSysDictCode)) {
                    it += (AuthUserAccountThirdPartys.subSysDictCode eq subSysDictCode!!)
                }
                if (StringKit.isNotBlank(ownerId)) {
                    it += (AuthUserAccountThirdPartys.ownerId eq ownerId!!)
                }
            }
            .totalRecords != 0
    }

    fun save(userAccountThirdParty: AuthUserAccountThirdParty): Boolean {
        return RdbKit.getDatabase().sequenceOf(AuthUserAccountThirdPartys).add(userAccountThirdParty) == 1
    }

    //endregion your codes 2

}