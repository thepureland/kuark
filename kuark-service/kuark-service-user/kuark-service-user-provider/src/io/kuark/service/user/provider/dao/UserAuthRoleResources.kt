package io.kuark.service.user.provider.dao

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.service.user.provider.po.UserAuthRoleResource
import me.liuwj.ktorm.schema.varchar

/**
 * 角色-资源关系数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserAuthRoleResources: StringIdTable<UserAuthRoleResource>("auth_role_resource") {
//endregion your codes 1

    /** 角色id */
    var roleId = varchar("role_id").bindTo { it.roleId }

    /** 资源id */
    var resourceId = varchar("resource_id").bindTo { it.resourceId }


    //region your codes 2

	//endregion your codes 2

}