package org.kuark.biz.sys.dao

import me.liuwj.ktorm.schema.varchar
import org.kuark.biz.sys.po.SysDict
import org.kuark.data.jdbc.support.MaintainableTable

object SysDicts: MaintainableTable<SysDict>("sys_dict") {

    val module = varchar("module").bindTo { it.module }
    val dictType = varchar("dict_type").bindTo { it.dictType }
    val dictName = varchar("dict_name").bindTo { it.dictName }

}