package io.kuark.tools.codegen.model.table

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.tools.codegen.model.po.CodeGenColumn
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * 代码生成-列信息数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object CodeGenColumns : StringIdTable<CodeGenColumn>("code_gen_column") {
//endregion your codes 1

    /** 字段名 */
    var name = varchar("name").bindTo { it.name }

    /** 对象名称 */
    var objectName = varchar("object_name").bindTo { it.objectName }

    /** 注释 */
    var comment = varchar("comment").bindTo { it.comment }

    /** 是否可查询 */
    var isSearchable = boolean("is_searchable").bindTo { it.isSearchable }

    /** 是否列表中可排序 */
    var isSortable = boolean("is_sortable").bindTo { it.isSortable }

    /** 列表中列序 */
    var orderInList = int("order_in_list").bindTo { it.orderInList }

    /** 列表默认排序 */
    var defaultOrder = varchar("default_order").bindTo { it.defaultOrder }

    /** 编辑页中的顺序 */
    var orderInEdit = int("order_in_edit").bindTo { it.orderInEdit }

    /** 详情页中的顺序 */
    var orderInView = int("order_in_view").bindTo { it.orderInView }


    //region your codes 2

    //endregion your codes 2

}