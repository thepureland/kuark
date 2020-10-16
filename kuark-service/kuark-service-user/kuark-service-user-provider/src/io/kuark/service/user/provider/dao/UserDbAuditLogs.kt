package io.kuark.service.user.provider.dao

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.service.user.provider.po.UserDbAuditLog
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * 用户数据库操作审计日志数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserDbAuditLogs: StringIdTable<UserDbAuditLog>("user_db_audit_log") {
//endregion your codes 1

    /** 外键，用户账号id，user_account表主键 */
    var userAccountId = varchar("user_account_id").bindTo { it.userAccountId }

    /** 表名 */
    var table = varchar("table_name").bindTo { it.tableName } // tableName被Ktorm占用

    /** 操作时间 */
    var operateTime = datetime("operate_time").bindTo { it.operateTime }

    /** 操作类型代码 */
    var operateTypeDictCode = varchar("operate_type_dict_code").bindTo { it.operateTypeDictCode }


    //region your codes 2

	//endregion your codes 2

}