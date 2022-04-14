package org.kuark.demo.tools.codegen.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity

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
    var comment: String?

    /** 是否查询项 */
    var searchItem: Boolean

    /** 是否列表项 */
    var listItem: Boolean

    /** 是否编辑项 */
    var editItem: Boolean

    /** 是否详情项 */
    var detailItem: Boolean


    //region your codes 2

    //endregion your codes 2

}