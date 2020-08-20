package io.kuark.resource.auth.po

import io.kuark.data.jdbc.support.DbEntityFactory
import io.kuark.data.jdbc.support.IMaintainableDbEntity

/**
 * 角色数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface AuthRole: IMaintainableDbEntity<String, AuthRole> {
//endregion your codes 1

    companion object : DbEntityFactory<AuthRole>()

    /** 角色名 */
    var roleName: String

    /** 子系统代码 */
    var subSysDictCode: String

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String


    //region your codes 2

	//endregion your codes 2

}