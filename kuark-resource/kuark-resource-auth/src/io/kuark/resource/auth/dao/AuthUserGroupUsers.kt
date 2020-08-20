package io.kuark.resource.auth.dao

import me.liuwj.ktorm.schema.*
import io.kuark.resource.auth.po.AuthUserGroupUser
import io.kuark.data.jdbc.support.StringIdTable

/**
 * 用户组-用户关系数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object AuthUserGroupUsers: StringIdTable<AuthUserGroupUser>("auth_user_group_user") {
//endregion your codes 1

    /** 用户组id */
    var groupId = varchar("group_id").bindTo { it.groupId }

    /** 用户id */
    var userId = varchar("user_id").bindTo { it.userId }


    //region your codes 2

	//endregion your codes 2

}