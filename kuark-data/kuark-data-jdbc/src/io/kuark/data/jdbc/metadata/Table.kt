package io.kuark.data.jdbc.metadata

/**
 * 关系型数据库表对象信息
 *
 * @author K
 * @since 1.0.0
 */
class Table {

    lateinit var name: String
    var comment: String? = null
    var schema: String? = null
    var cat: String? = null
    lateinit var type: TableType

}