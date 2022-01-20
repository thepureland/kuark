package io.kuark.service.user.provider.rbac.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.user.provider.rbac.model.po.RbacRoleUser
import io.kuark.service.user.provider.rbac.model.table.RbacRoleUsers
import org.springframework.stereotype.Repository

@Repository
open class RbacRoleUserDao: BaseCrudDao<String, RbacRoleUser, RbacRoleUsers>() {



}