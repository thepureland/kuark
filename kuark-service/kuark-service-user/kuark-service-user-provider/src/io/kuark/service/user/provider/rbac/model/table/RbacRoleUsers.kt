package io.kuark.service.user.provider.rbac.model.table

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.service.user.provider.rbac.model.po.RbacRoleUser
import org.ktorm.schema.varchar

/**
 * 角色-用户关系数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object RbacRoleUsers: StringIdTable<RbacRoleUser>("rbac_role_user") {
//endregion your codes 1

    /** 角色id */
    var roleId = varchar("role_id").bindTo { it.roleId }

    /** 用户id */
    var userId = varchar("user_id").bindTo { it.userId }


    //region your codes 2

	//endregion your codes 2

}