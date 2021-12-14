package io.kuark.service.sys.provider.user.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.sys.provider.user.model.po.UserAccountProtection
import io.kuark.service.sys.provider.user.model.table.UserAccountProtections
import org.springframework.stereotype.Repository

/**
 * 用户账号保护数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class UserAccountProtectionDao : BaseCrudDao<String, UserAccountProtection, UserAccountProtections>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}