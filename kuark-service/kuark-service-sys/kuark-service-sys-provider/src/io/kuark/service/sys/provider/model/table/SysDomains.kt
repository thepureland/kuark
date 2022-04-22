package io.kuark.service.sys.provider.model.table

import io.kuark.service.sys.provider.model.po.SysDomain
import org.ktorm.schema.*
import io.kuark.ability.data.rdb.support.MaintainableTable


/**
 * 域名数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object SysDomains : MaintainableTable<SysDomain>("sys_domain") {
//endregion your codes 1

    /** 域名 */
    var domain = varchar("domain").bindTo { it.domain }

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 租户id */
    var tenantId = varchar("tenant_id").bindTo { it.tenantId }


    //region your codes 2

    //endregion your codes 2

}