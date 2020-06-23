package org.kuark.biz.sys.dao

import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.kuark.biz.sys.po.SysResource
import org.kuark.data.jdbc.support.MaintainableTable

object SysResources: MaintainableTable<SysResource>("sys_resource") {

    val name by varchar("name").bindTo { it.name }
    val url by varchar("url").bindTo { it.url }
    val resourceTypeCode by varchar("resource_type_code").bindTo { it.resourceTypeCode }
    val parentId by varchar("parent_id").bindTo { it.parentId }
    val seqNo by int("seq_no").bindTo { it.seqNo }
    val subSysCode by varchar("sub_sys_code").bindTo { it.subSysCode }
    val permission by varchar("permission").bindTo { it.permission }
    val iconUrl by varchar("icon_url").bindTo { it.iconUrl }

}