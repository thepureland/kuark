package io.kuark.ability.auth.rbac.model.table

import io.kuark.ability.data.rdb.support.MaintainableTable
import io.kuark.ability.auth.rbac.model.po.AuthUserAccountThirdParty
import org.ktorm.schema.varchar

/**
 * 用户账号授权数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object AuthUserAccountThirdPartys: MaintainableTable<AuthUserAccountThirdParty>("user_account_auth") {
//endregion your codes 1

    /** 外键，用户账号id，user_account表主键 */
    var userAccountId = varchar("user_account_id").bindTo { it.userAccountId }

    /** 身份类型代码 */
    var identityTypeDictCode = varchar("identity_type_dict_code").bindTo { it.identityTypeDictCode }

    /** 唯一身份标识 */
    var identifier = varchar("identifier").bindTo { it.identifier }

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId = varchar("owner_id").bindTo { it.ownerId }


    //region your codes 2

	//endregion your codes 2

}