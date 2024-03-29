package io.kuark.service.user.provider.user.model.table

import io.kuark.ability.data.rdb.support.MaintainableTable
import io.kuark.service.user.provider.user.model.po.UserAccountThird
import org.ktorm.schema.varchar

/**
 * 用户账号第三方授权信息数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserAccountThirds: MaintainableTable<UserAccountThird>("user_account_third") {
//endregion your codes 1

    /** 外键，用户账号id，user_account表主键 */
    var userAccountId = varchar("user_account_id").bindTo { it.userAccountId }

    /** 身份类型代码 */
    var principalTypeDictCode = varchar("principal_type_dict_code").bindTo { it.principalTypeDictCode }

    /** 唯一身份标识 */
    var principal = varchar("principal").bindTo { it.principal }

    /** 凭证 */
    var credentials = varchar("credentials").bindTo { it.credentials }

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 租户id */
    var tenantId = varchar("tenant_id").bindTo { it.tenantId }


    //region your codes 2

	//endregion your codes 2

}