package io.kuark.ability.auth.rbac.model.table

import io.kuark.ability.auth.rbac.model.po.AuthPersistentLogins
import io.kuark.ability.data.rdb.support.StringIdTable
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * 登陆持久化数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object AuthPersistentLoginss : StringIdTable<AuthPersistentLogins>("auth_persistent_logins") {
//endregion your codes 1

    /** 用户名 */
    var username = varchar("username").bindTo { it.username }

    /** 自动登陆会话令牌，会在每一个新的session中都重新生成 */
    var token = varchar("token").bindTo { it.token }

    /** 最后一次使用时间 */
    var lastUsed = datetime("last_used").bindTo { it.lastUsed }


    //region your codes 2

    //endregion your codes 2

}