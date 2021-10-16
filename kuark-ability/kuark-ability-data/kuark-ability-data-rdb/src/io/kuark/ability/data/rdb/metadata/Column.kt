package io.kuark.ability.data.rdb.metadata

import io.kuark.base.lang.string.underscoreToHump
import java.sql.Types
import kotlin.reflect.KClass

/**
 * 关系型数据库表的列信息
 *
 * @author K
 * @since 1.0.0
 */
class Column {

    /** 列名 */
    lateinit var name: String

    /** 注释 */
    var comment: String? = null

    /** JDBC类型 */
    var jdbcType: Int = Types.VARCHAR

    /** JDBC类型名称 */
    lateinit var jdbcTypeName: String

    /** JDBC类型对应的kotlin中的类 */
    lateinit var kotlinType: KClass<*>

    /** 长度 */
    var length: Int? = null

    /** 小数位数 */
    var decimalDigits: Int? = null

    /** 默认值 */
    var defaultValue: String? = null

    /** 是否可以为null */
    var nullable: Boolean = true

    /** 是否为主键 */
    var primaryKey: Boolean = false

    /** 是否为外键 */
    var foreignKey: Boolean = false

    /** 是否有索引 */
    var indexed: Boolean = false

    /** 是否惟一 */
    var unique: Boolean = false

    /** 是否为字典代码 */
    var dictCode: Boolean = false

    /** 是否为乍增长 */
    var autoIncrement: String? = null

    /**
     * 返回kotlin类型的简短名称
     */
    fun getKotlinTypeName(): String = kotlinType.simpleName!!

    /**
     * 返回列名的驼峰写法表示
     */
    fun getColumnHumpName(): String = name.underscoreToHump()

    /**
     * 返回Ktorm框架对应的sql类型的函数名
     */
    fun getKtormSqlTypeFunName(): String = KtormSqlType.getFunName(kotlinType)

}