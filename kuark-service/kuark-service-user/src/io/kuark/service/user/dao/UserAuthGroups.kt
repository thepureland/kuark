package io.kuark.service.user.dao

import io.kuark.ability.data.jdbc.support.MaintainableTable
import io.kuark.service.user.po.UserAuthGroup
import me.liuwj.ktorm.schema.varchar

/**
 * 用户组数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserAuthGroups: MaintainableTable<UserAuthGroup>("auth_user_group") {
//endregion your codes 1

    /** 用户组名 */
    var groupName = varchar("group_name").bindTo { it.groupName }

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId = varchar("owner_id").bindTo { it.ownerId }


    //region your codes 2

	//endregion your codes 2

}