package io.kuark.resource.auth.po

import io.kuark.data.jdbc.support.DbEntityFactory
import io.kuark.data.jdbc.support.IDbEntity

/**
 * 角色-用户关系数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface AuthRoleUser: IDbEntity<String, AuthRoleUser> {
//endregion your codes 1

    companion object : DbEntityFactory<AuthRoleUser>()

    /** 角色id */
    var roleId: String

    /** 用户id */
    var userId: String


    //region your codes 2

	//endregion your codes 2

}