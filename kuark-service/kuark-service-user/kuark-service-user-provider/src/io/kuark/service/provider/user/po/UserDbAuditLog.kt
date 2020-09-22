package io.kuark.service.provider.user.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity
import java.time.LocalDateTime

/**
 * 用户数据库操作审计日志数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserDbAuditLog: IDbEntity<String, UserDbAuditLog> {
//endregion your codes 1

    companion object : DbEntityFactory<UserDbAuditLog>()

    /** 外键，用户账号id，user_account表主键 */
    var userAccountId: String

    /** 表名 */
    var tableName: String

    /** 操作时间 */
    var operateTime: LocalDateTime

    /** 操作类型代码 */
    var operateTypeDictCode: String


    //region your codes 2

	//endregion your codes 2

}