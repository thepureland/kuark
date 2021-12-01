package io.kuark.service.sys.provider.user.model.table

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.service.sys.provider.user.model.po.UserDbAuditLogItem
import org.ktorm.schema.varchar

/**
 * 用户数据库操作审计日志明细数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserDbAuditLogItems: StringIdTable<UserDbAuditLogItem>("user_db_audit_log_item") {
//endregion your codes 1

    /** 外键，数据库操作审计日志id，user_db_audit_log表主键 */
    var dbAuditLogId = varchar("db_audit_log_id").bindTo { it.dbAuditLogId }

    /** 记录的主键值 */
    var recordId = varchar("record_id").bindTo { it.recordId }

    /** update的列名 */
    var columnName = varchar("column_name").bindTo { it.columnName }

    /** update前的值 */
    var oldValue = varchar("old_value").bindTo { it.oldValue }

    /** update后的值 */
    var newValue = varchar("new_value").bindTo { it.newValue }


    //region your codes 2

	//endregion your codes 2

}