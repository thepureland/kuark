package io.kuark.service.auth.provider.rbac.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.service.auth.provider.rbac.model.po.AuthRole
import io.kuark.service.auth.provider.rbac.model.table.AuthRoles
import org.springframework.stereotype.Repository

/**
 * 角色数据访问对象
 *
 * @author hanfei
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class AuthRoleDao : BaseDao<String, AuthRole, AuthRoles>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}