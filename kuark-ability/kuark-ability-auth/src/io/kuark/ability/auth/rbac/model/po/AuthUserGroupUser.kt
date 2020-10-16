package io.kuark.ability.auth.rbac.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity

/**
 * 用户组-用户关系数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface AuthUserGroupUser: IDbEntity<String, AuthUserGroupUser> {
//endregion your codes 1

    companion object : DbEntityFactory<AuthUserGroupUser>()

    /** 用户组id */
    var groupId: String

    /** 用户id */
    var userId: String


    //region your codes 2

	//endregion your codes 2

}