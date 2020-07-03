package org.kuark.biz.sys.dao

import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.kuark.biz.sys.po.SysResource
import org.kuark.data.jdbc.support.MaintainableTable

object SysResources: MaintainableTable<SysResource>("sys_resource") {

    val name = varchar("name").bindTo { it.name }
    val url = varchar("url").bindTo { it.url }
    val resourceTypeCode = varchar("resource_type_code").bindTo { it.resourceTypeCode }
    val parentId = varchar("parent_id").bindTo { it.parentId }
    val seqNo = int("seq_no").bindTo { it.seqNo }
    val subSysCode = varchar("sub_sys_code").bindTo { it.subSysCode }
    val permission = varchar("permission").bindTo { it.permission }
    val iconUrl = varchar("icon_url").bindTo { it.iconUrl }

}