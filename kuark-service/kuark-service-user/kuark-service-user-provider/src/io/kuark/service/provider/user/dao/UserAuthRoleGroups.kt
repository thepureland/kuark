package io.kuark.service.provider.user.dao

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.service.user.po.UserAuthRoleGroup
import me.liuwj.ktorm.schema.varchar

/**
 * 角色-用户组关系数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserAuthRoleGroups: StringIdTable<UserAuthRoleGroup>("auth_role_user_group") {
//endregion your codes 1

    /** 角色id */
    var roleId = varchar("role_id").bindTo { it.roleId }

    /** 用户组id */
    var groupId = varchar("group_id").bindTo { it.groupId }


    //region your codes 2

	//endregion your codes 2

}