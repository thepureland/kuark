package org.kuark.resource.auth.po

import org.kuark.data.jdbc.support.DbEntityFactory
import org.kuark.data.jdbc.support.IDbEntity

/**
 * 角色-资源关系数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface AuthRoleResource: IDbEntity<String, AuthRoleResource> {
//endregion your codes 1

    companion object : DbEntityFactory<AuthRoleResource>()

    /** 角色id */
    var roleId: String

    /** 资源id */
    var resourceId: String


    //region your codes 2

	//endregion your codes 2

}