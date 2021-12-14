package io.kuark.service.auth.provider.rbac.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.auth.provider.rbac.model.po.AuthUserGroup
import io.kuark.service.auth.provider.rbac.model.table.AuthUserGroups
import org.springframework.stereotype.Repository

/**
 * 用户组数据访问对象
 *
 * @author hanfei
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class AuthUserGroupDao : BaseCrudDao<String, AuthUserGroup, AuthUserGroups>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}