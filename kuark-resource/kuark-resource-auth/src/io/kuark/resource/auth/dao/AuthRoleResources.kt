package io.kuark.resource.auth.dao

import me.liuwj.ktorm.schema.*
import io.kuark.resource.auth.po.AuthRoleResource
import io.kuark.data.jdbc.support.StringIdTable

/**
 * 角色-资源关系数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object AuthRoleResources: StringIdTable<AuthRoleResource>("auth_role_resource") {
//endregion your codes 1

    /** 角色id */
    var roleId = varchar("role_id").bindTo { it.roleId }

    /** 资源id */
    var resourceId = varchar("resource_id").bindTo { it.resourceId }


    //region your codes 2

	//endregion your codes 2

}