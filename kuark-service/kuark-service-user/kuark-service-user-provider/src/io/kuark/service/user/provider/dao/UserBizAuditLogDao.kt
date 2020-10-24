package io.kuark.service.user.provider.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.service.user.provider.model.po.UserBizAuditLog
import io.kuark.service.user.provider.model.table.UserBizAuditLogs
import org.ktorm.dsl.*
import org.springframework.stereotype.Repository

/**
 * 用户审计日志数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class UserBizAuditLogDao : BaseDao<String, UserBizAuditLog, UserBizAuditLogs>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}