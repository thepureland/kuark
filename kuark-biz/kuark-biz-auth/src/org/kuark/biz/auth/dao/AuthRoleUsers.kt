package org.kuark.biz.auth.dao

import me.liuwj.ktorm.schema.*
import org.kuark.biz.auth.po.AuthRoleUser
import org.kuark.data.jdbc.support.StringIdTable

/**
 * 角色-用户关系数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object AuthRoleUsers: StringIdTable<AuthRoleUser>("auth_role_user") {
//endregion your codes 1

	/** 角色id */
	var roleId = varchar("role_id").bindTo { it.roleId }
	/** 用户id */
	var userId = varchar("user_id").bindTo { it.userId }

	//region your codes 2

	//endregion your codes 2

}