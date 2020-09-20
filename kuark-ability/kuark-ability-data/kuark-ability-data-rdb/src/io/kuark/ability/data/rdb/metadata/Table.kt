package io.kuark.ability.data.rdb.metadata

/**
 * 关系型数据库表对象信息
 *
 * @author K
 * @since 1.0.0
 */
class Table {

    /** 表名 */
    lateinit var name: String

    /** 注释 */
    var comment: String? = null

    /** 所属Schema */
    var schema: String? = null

    /** 所属Catalog */
    var catalog: String? = null

    /** 表类型 */
    lateinit var type: TableType

}