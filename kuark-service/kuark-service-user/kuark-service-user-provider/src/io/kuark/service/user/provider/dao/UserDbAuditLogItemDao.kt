package io.kuark.service.user.provider.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.service.user.provider.model.po.UserDbAuditLogItem
import io.kuark.service.user.provider.model.table.UserDbAuditLogItems
import org.springframework.stereotype.Repository

/**
 * 用户数据库操作审计日志明细数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class UserDbAuditLogItemDao : BaseDao<String, UserDbAuditLogItem, UserDbAuditLogItems>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}