package io.kuark.service.auth.provider.rbac.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity
import java.time.LocalDateTime

/**
 * 登陆持久化数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface AuthPersistentLogins : IDbEntity<String, AuthPersistentLogins> {
//endregion your codes 1

    companion object : DbEntityFactory<AuthPersistentLogins>()

    /** 用户名 */
    var username: String

    /** 自动登陆会话令牌，会在每一个新的session中都重新生成 */
    var token: String?

    /** 最后一次使用时间 */
    var lastUsed: LocalDateTime?


    //region your codes 2

    //endregion your codes 2

}