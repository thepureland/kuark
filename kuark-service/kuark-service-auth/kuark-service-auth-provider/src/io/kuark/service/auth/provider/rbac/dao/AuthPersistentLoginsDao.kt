package io.kuark.service.auth.provider.rbac.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.auth.provider.rbac.model.po.AuthPersistentLogins
import io.kuark.service.auth.provider.rbac.model.table.AuthPersistentLoginss
import org.springframework.stereotype.Repository

/**
 * 登陆持久化数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class AuthPersistentLoginsDao : BaseCrudDao<String, AuthPersistentLogins, AuthPersistentLoginss>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}