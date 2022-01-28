package io.kuark.service.user.provider.user.dao

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.base.lang.string.StringKit
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.user.provider.user.model.po.UserAccountThird
import io.kuark.service.user.provider.user.model.table.UserAccountThirds
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
open class UserAccountThirdDao : BaseCrudDao<String, UserAccountThird, UserAccountThirds>() {
//endregion your codes 1

    //region your codes 2

    fun isIdentifierExists(identityTypeDictCode: String, identifier: String): Boolean {
        val context = KuarkContextHolder.get()
        val subSysDictCode = context.subSysCode
        val tenantId = context.tenantId

        return querySource()
            .select(UserAccountThirds.id)
            .whereWithConditions {
                it += (UserAccountThirds.principal eq identifier) and
                        (UserAccountThirds.principalTypeDictCode eq identityTypeDictCode) and
                        (UserAccountThirds.active eq true)
                if (StringKit.isNotBlank(subSysDictCode)) {
                    it += (UserAccountThirds.subSysDictCode eq subSysDictCode!!)
                }
                if (StringKit.isNotBlank(tenantId)) {
                    it += (UserAccountThirds.tenantId eq tenantId!!)
                }
            }
            .totalRecords != 0
    }

    fun save(userAccountThird: UserAccountThird): Boolean {
        return RdbKit.getDatabase().sequenceOf(UserAccountThirds).add(userAccountThird) == 1
    }

    //endregion your codes 2

}