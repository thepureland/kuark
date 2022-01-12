package io.kuark.service.user.provider.rbac.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 用户组数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface RbacUserGroup: IMaintainableDbEntity<String, RbacUserGroup> {
//endregion your codes 1

    companion object : DbEntityFactory<RbacUserGroup>()

    /** 用户组编码 */
    var groupCode: String

    /** 用户组名 */
    var groupName: String

    /** 子系统代码 */
    var subSysDictCode: String?

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String?


    //region your codes 2

	//endregion your codes 2

}