package io.kuark.service.user.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity

/**
 * 角色-用户关系数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserAuthRoleUser: IDbEntity<String, UserAuthRoleUser> {
//endregion your codes 1

    companion object : DbEntityFactory<UserAuthRoleUser>()

    /** 角色id */
    var roleId: String

    /** 用户id */
    var userId: String


    //region your codes 2

	//endregion your codes 2

}