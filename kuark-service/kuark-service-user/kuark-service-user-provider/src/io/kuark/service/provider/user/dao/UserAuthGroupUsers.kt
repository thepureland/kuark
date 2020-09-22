package io.kuark.service.provider.user.dao

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.service.user.po.UserAuthGroupUser
import me.liuwj.ktorm.schema.varchar

/**
 * 用户组-用户关系数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserAuthGroupUsers: StringIdTable<UserAuthGroupUser>("auth_user_group_user") {
//endregion your codes 1

    /** 用户组id */
    var groupId = varchar("group_id").bindTo { it.groupId }

    /** 用户id */
    var userId = varchar("user_id").bindTo { it.userId }


    //region your codes 2

	//endregion your codes 2

}