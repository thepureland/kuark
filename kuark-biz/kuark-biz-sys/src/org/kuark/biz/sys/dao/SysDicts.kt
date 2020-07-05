package org.kuark.biz.sys.dao

import me.liuwj.ktorm.schema.*
import org.kuark.biz.sys.po.SysDict
import org.kuark.data.jdbc.support.MaintainableTable

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