package io.kuark.ability.auth.rbac.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.ability.auth.rbac.model.po.AuthPersistentLogins
import io.kuark.ability.auth.rbac.model.table.AuthPersistentLoginss
import org.ktorm.dsl.*
import org.springframework.stereotype.Repository

/**
 * 登陆持久化数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class AuthPersistentLoginsDao : BaseDao<String, AuthPersistentLogins, AuthPersistentLoginss>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}