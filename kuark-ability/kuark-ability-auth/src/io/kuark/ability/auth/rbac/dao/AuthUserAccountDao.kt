package io.kuark.ability.auth.rbac.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.ability.auth.rbac.model.po.AuthUserAccount
import io.kuark.ability.auth.rbac.model.table.AuthUserAccounts
import org.ktorm.dsl.*
import org.springframework.stereotype.Repository

/**
 * 用户账号数据访问对象
 *
 * @author hanfei
 * @since 1.0.0
 */
@Repository
//region your codes 1
class AuthUserAccountDao: BaseDao<String, AuthUserAccount, AuthUserAccounts>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}