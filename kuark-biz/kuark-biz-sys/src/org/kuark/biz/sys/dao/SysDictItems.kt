package org.kuark.biz.sys.dao

import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.kuark.biz.sys.po.SysDictItem
import org.kuark.data.jdbc.support.MaintainableTable

object SysDictItems: MaintainableTable<SysDictItem>("sys_dict_item") {

    val itemCode = varchar("item_code").bindTo { it.itemCode }
    val parentCode = varchar("parent_code").bindTo { it.parentCode }
    val itemName = varchar("item_name").bindTo { it.itemName }
    val seqNo = int("seq_no").bindTo { it.seqNo }
    val sysDictId = int("sys_dict_id").references(SysDicts) { it.sysDict }

}