package io.kuark.service.user.provider.rbac.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.user.provider.rbac.model.po.RbacRoleResource
import io.kuark.service.user.provider.rbac.model.table.RbacRoleResources
import org.springframework.stereotype.Repository

@Repository
open class RbacRoleResourceDao: BaseCrudDao<String, RbacRoleResource, RbacRoleResources>() {



}