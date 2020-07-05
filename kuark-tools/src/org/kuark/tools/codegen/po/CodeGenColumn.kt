package org.kuark.tools.codegen.po

import org.kuark.data.jdbc.support.DbEntityFactory
import org.kuark.data.jdbc.support.IDbEntity

/**
 * 代码生成-列信息数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface CodeGenColumn : IDbEntity<String, CodeGenColumn> {
//endregion your codes 1

    companion object : DbEntityFactory<CodeGenColumn>()

    /** 字段名 */
    var name: String

    /** 对象名称 */
    var objectName: String

    /** 注释 */
    var comment: String

    /** 是否可查询 */
    var isSearchable: Boolean

    /** 是否列表中可排序 */
    var isSortable: Boolean

    /** 列表中列序 */
    var orderInList: Int

    /** 列表默认排序 */
    var defaultOrder: String

    /** 编辑页中的顺序 */
    var orderInEdit: Int

    /** 详情页中的顺序 */
    var orderInView: Int


    //region your codes 2

    //endregion your codes 2

}