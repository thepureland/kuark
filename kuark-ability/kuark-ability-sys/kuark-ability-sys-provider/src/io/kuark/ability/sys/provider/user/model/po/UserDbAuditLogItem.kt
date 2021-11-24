package io.kuark.ability.sys.provider.user.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity

/**
 * 用户数据库操作审计日志明细数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserDbAuditLogItem: IDbEntity<String, io.kuark.ability.sys.provider.user.model.po.UserDbAuditLogItem> {
//endregion your codes 1

    companion object : DbEntityFactory<io.kuark.ability.sys.provider.user.model.po.UserDbAuditLogItem>()

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