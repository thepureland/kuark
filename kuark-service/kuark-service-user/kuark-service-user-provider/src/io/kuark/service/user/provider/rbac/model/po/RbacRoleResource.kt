package io.kuark.service.user.provider.rbac.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity

/**
 * 角色-资源关系数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface RbacRoleResource: IDbEntity<String, RbacRoleResource> {
//endregion your codes 1

    companion object : DbEntityFactory<RbacRoleResource>()

    /** 角色id */
    var roleId: String

    /** 资源id */
    var resourceId: String


    //region your codes 2

	//endregion your codes 2

}