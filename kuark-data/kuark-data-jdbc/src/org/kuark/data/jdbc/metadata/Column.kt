package org.kuark.data.jdbc.metadata

import java.sql.Types
import kotlin.reflect.KClass

class Column {

    lateinit var name: String
    var comment: String? = null
    var jdbcType: Int = Types.VARCHAR
    lateinit var jdbcTypeName: String
    lateinit var kotlinType: KClass<*>
    var length: Int? = null
    var decimalDigits: Int? = null
    var defaultValue: String? = null
    var isNullable: Boolean = true
    var isPrimaryKey: Boolean = false
    var isForeignKey: Boolean = false
    var isIndexed: Boolean = false
    var isUnique: Boolean = false
    var isDictCode: Boolean = false
    var autoIncrement: String? = null

}