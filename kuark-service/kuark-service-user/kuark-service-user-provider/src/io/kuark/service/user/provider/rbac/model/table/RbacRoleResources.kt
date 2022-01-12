package io.kuark.service.user.provider.rbac.model.table

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.service.user.provider.rbac.model.po.RbacRoleResource
import org.ktorm.schema.varchar

/**
 * 角色-资源关系数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object RbacRoleResources: StringIdTable<RbacRoleResource>("rbac_role_resource") {
//endregion your codes 1

    /** 角色id */
    var roleId = varchar("role_id").bindTo { it.roleId }

    /** 资源id */
    var resourceId = varchar("resource_id").bindTo { it.resourceId }


    //region your codes 2

	//endregion your codes 2

}