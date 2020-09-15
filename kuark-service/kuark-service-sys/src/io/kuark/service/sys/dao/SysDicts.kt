package io.kuark.service.sys.dao

import io.kuark.ability.data.jdbc.support.MaintainableTable
import io.kuark.service.sys.po.SysDict
import me.liuwj.ktorm.schema.varchar

/**
 * 字典主表数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object SysDicts: MaintainableTable<SysDict>("sys_dict") {
//endregion your codes 1

    /** 模块 */
    var module = varchar("module").bindTo { it.module }

    /** 字典类型 */
    var dictType = varchar("dict_type").bindTo { it.dictType }

    /** 字典名称，或其国际化key */
    var dictName = varchar("dict_name").bindTo { it.dictName }


    //region your codes 2

	//endregion your codes 2

}