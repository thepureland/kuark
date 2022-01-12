package io.kuark.service.user.provider.user.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.user.provider.user.model.po.UserDbAuditLogItem
import io.kuark.service.user.provider.user.model.table.UserDbAuditLogItems
import org.springframework.stereotype.Repository

/**
 * 用户数据库操作审计日志明细数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class UserDbAuditLogItemDao : BaseCrudDao<String, UserDbAuditLogItem, UserDbAuditLogItems>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}