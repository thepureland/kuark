package io.kuark.service.user.provider.rbac.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import io.kuark.service.user.provider.rbac.model.table.RbacRoles
import org.springframework.stereotype.Repository

/**
 * 角色数据访问对象
 *
 * @author hanfei
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class RbacRoleDao : BaseCrudDao<String, RbacRole, RbacRoles>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}