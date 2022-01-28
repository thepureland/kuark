package io.kuark.service.user.provider.rbac.model.table

import io.kuark.ability.data.rdb.support.MaintainableTable
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import org.ktorm.schema.varchar

/**
 * 角色数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object RbacRoles: MaintainableTable<RbacRole>("rbac_role") {
//endregion your codes 1

    /** 角色编码 */
    var roleCode = varchar("role_code").bindTo { it.roleCode }

    /** 角色名 */
    var roleName = varchar("role_name").bindTo { it.roleName }

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 租户id */
    var tenantId = varchar("tenant_id").bindTo { it.tenantId }


    //region your codes 2

	//endregion your codes 2

}