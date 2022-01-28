package io.kuark.service.sys.provider.model.table

import io.kuark.service.sys.provider.model.po.SysTenant
import org.ktorm.schema.*
import io.kuark.ability.data.rdb.support.MaintainableTable


/**
 * 租户数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object SysTenants : MaintainableTable<SysTenant>("sys_tenant") {
//endregion your codes 1

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 名称 */
    var name = varchar("name").bindTo { it.name }


    //region your codes 2

    //endregion your codes 2

}