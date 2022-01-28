package io.kuark.service.user.provider.rbac.model.table

import io.kuark.ability.data.rdb.support.MaintainableTable
import io.kuark.service.user.provider.rbac.model.po.RbacUserGroup
import org.ktorm.schema.varchar

/**
 * 用户组数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object RbacUserGroups: MaintainableTable<RbacUserGroup>("rbac_user_group") {
//endregion your codes 1

    /** 用户组编码 */
    var groupCode = varchar("group_code").bindTo { it.groupCode }

    /** 用户组名 */
    var groupName = varchar("group_name").bindTo { it.groupName }

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 租户id */
    var tenantId = varchar("tenant_id").bindTo { it.tenantId }


    //region your codes 2

	//endregion your codes 2

}