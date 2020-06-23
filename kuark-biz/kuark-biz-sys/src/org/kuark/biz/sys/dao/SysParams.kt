package org.kuark.biz.sys.dao

import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.kuark.biz.sys.po.SysParam
import org.kuark.data.jdbc.support.MaintainableTable

object SysParams: MaintainableTable<SysParam>("sys_param") {

    val module by varchar("module").bindTo { it.module }
    val paramName by varchar("param_name").bindTo { it.paramName }
    val paramValue by varchar("param_value").bindTo { it.paramValue }
    val defaultValue by varchar("default_value").bindTo { it.defaultValue }
    val seqNo by int("seq_no").bindTo { it.seqNo }

}