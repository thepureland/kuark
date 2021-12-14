package io.kuark.service.sys.provider.user.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.sys.provider.user.model.po.UserBizAuditLog
import io.kuark.service.sys.provider.user.model.table.UserBizAuditLogs
import org.springframework.stereotype.Repository

/**
 * 用户审计日志数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class UserBizAuditLogDao : BaseCrudDao<String, UserBizAuditLog, UserBizAuditLogs>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}