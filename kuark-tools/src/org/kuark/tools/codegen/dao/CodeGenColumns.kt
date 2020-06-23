package org.kuark.tools.codegen.dao

import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.kuark.data.jdbc.support.StringIdTable
import org.kuark.tools.codegen.po.CodeGenColumn

object CodeGenColumns : StringIdTable<CodeGenColumn>("code_gen_column") {

    val name by varchar("name").bindTo { it.name }
    val objectName by varchar("object_name").bindTo { it.objectName }
    val comment by varchar("comment").bindTo { it.comment }
    val isSearchable by boolean("is_searchable").bindTo { it.isSearchable }
    val isSortable by boolean("is_sortable").bindTo { it.isSortable }
    val orderInList by int("order_in_list").bindTo { it.orderInList }
    val defaultOrder by int("default_order").bindTo { it.defaultOrder }
    val orderInEdit by int("order_in_edit").bindTo { it.orderInEdit }
    val orderInView by int("order_in_edit").bindTo { it.orderInView }

}