package org.kuark.data.jdbc.metadata

import org.kuark.base.lang.string.underscoreToHump
import java.sql.Types
import kotlin.reflect.KClass

/**
 * 关系型数据库表的列信息
 *
 * @author K
 * @since 1.0.0
 */
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

    fun getKotlinTypeName(): String = kotlinType.simpleName!!

    fun getColumnHumpName(): String = name.underscoreToHump()

    fun getKtormSqlTypeFunName(): String = KtormSqlType.getFunName(kotlinType)

}