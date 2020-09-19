package io.kuark.service.user.po

import io.kuark.ability.data.jdbc.support.DbEntityFactory
import io.kuark.ability.data.jdbc.support.IDbEntity

/**
 * 用户数据库操作审计日志明细数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserDbAuditLogItem: IDbEntity<String, UserDbAuditLogItem> {
//endregion your codes 1

    companion object : DbEntityFactory<UserDbAuditLogItem>()

    /** 外键，数据库操作审计日志id，user_db_audit_log表主键 */
    var dbAuditLogId: String

    /** 记录的主键值 */
    var recordId: String

    /** update的列名 */
    var columnName: String?

    /** update前的值 */
    var oldValue: String?

    /** update后的值 */
    var newValue: String?


    //region your codes 2

	//endregion your codes 2

}