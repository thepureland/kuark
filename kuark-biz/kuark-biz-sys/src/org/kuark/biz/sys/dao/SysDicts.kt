package org.kuark.biz.sys.dao

import me.liuwj.ktorm.schema.varchar
import org.kuark.biz.sys.dao.SysDataSources.bindTo
import org.kuark.biz.sys.po.SysDict
import org.kuark.data.jdbc.support.MaintainableTable

object SysDicts: MaintainableTable<SysDict>("sys_dict") {

    val module by varchar("module").bindTo { it.module }
    val dictType by varchar("dict_type").bindTo { it.dictType }
    val dictName by varchar("dict_name").bindTo { it.dictName }

}