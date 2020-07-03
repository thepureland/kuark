package org.kuark.tools.codegen.dao

import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.kuark.data.jdbc.support.StringIdTable
import org.kuark.tools.codegen.po.CodeGenColumn

object CodeGenColumns : StringIdTable<CodeGenColumn>("code_gen_column") {

    val name = varchar("name").bindTo { it.name }
    val objectName = varchar("object_name").bindTo { it.objectName }
    val comment = varchar("comment").bindTo { it.comment }
    val isSearchable = boolean("is_searchable").bindTo { it.isSearchable }
    val isSortable = boolean("is_sortable").bindTo { it.isSortable }
    val orderInList = int("order_in_list").bindTo { it.orderInList }
    val defaultOrder = int("default_order").bindTo { it.defaultOrder }
    val orderInEdit = int("order_in_edit").bindTo { it.orderInEdit }
    val orderInView = int("order_in_view").bindTo { it.orderInView }

}