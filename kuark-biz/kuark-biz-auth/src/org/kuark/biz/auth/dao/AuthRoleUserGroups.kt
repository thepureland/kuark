package org.kuark.biz.auth.dao

import me.liuwj.ktorm.schema.*
import org.kuark.biz.auth.po.AuthRoleUserGroup
import org.kuark.data.jdbc.support.StringIdTable

/**
 * 角色-用户组关系数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object AuthRoleUserGroups: StringIdTable<AuthRoleUserGroup>("auth_role_user_group") {
//endregion your codes 1

    /** 角色id */
    var roleId = varchar("role_id").bindTo { it.roleId }

    /** 用户组id */
    var groupId = varchar("group_id").bindTo { it.groupId }


    //region your codes 2

	//endregion your codes 2

}