package io.kuark.service.user.provider.rbac.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 角色数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface RbacRole: IMaintainableDbEntity<String, RbacRole> {
//endregion your codes 1

    companion object : DbEntityFactory<RbacRole>()

    /** 角色编码 */
    var roleCode: String

    /** 角色名 */
    var roleName: String

    /** 子系统代码 */
    var subSysDictCode: String?

    /** 租户id */
    var tenantId: String?


    //region your codes 2

	//endregion your codes 2

}