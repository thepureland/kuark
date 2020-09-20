package io.kuark.ability.data.rdb.metadata

import java.sql.Types
import kotlin.reflect.KClass

/**
 * jdbc类型和kotlin类型的映射
 *
 * @author K
 * @since 1.0.0
 */
object JdbcTypeToKotlinType {

    private val defaultMapping: Map<Int, KClass<*>> = mapOf(
        Types.ARRAY to Array<Any>::class,
        Types.BIGINT to Long::class,
        Types.BINARY to Array<Byte>::class,
        Types.BIT to Boolean::class,
        Types.BLOB to java.sql.Blob::class,
        Types.BOOLEAN to Boolean::class,
        Types.CHAR to String::class,
        Types.CLOB to java.sql.Clob::class,
        Types.DATALINK to Any::class,
        Types.DATE to java.time.LocalDate::class,
        Types.DECIMAL to java.math.BigDecimal::class,
        Types.DISTINCT to Any::class,
        Types.DOUBLE to Double::class,
        Types.FLOAT to Double::class,
        Types.INTEGER to Int::class,
        Types.JAVA_OBJECT to Any::class,
        Types.LONGNVARCHAR to String::class,
        Types.LONGVARBINARY to Array<Byte>::class,
        Types.LONGVARCHAR to String::class,
        Types.NCHAR to String::class,
        Types.NCLOB to java.sql.Clob::class,
        Types.NULL to Nothing::class,
        Types.NUMERIC to Double::class,
        Types.NVARCHAR to String::class,
        Types.OTHER to Any::class,
        Types.REAL to Float::class,
        Types.REF to java.sql.Ref::class,
        Types.REF_CURSOR to Any::class,
        Types.ROWID to java.sql.RowId::class,
//        Types.SMALLINT to Short::class,
        Types.SMALLINT to Int::class,
        Types.SQLXML to java.sql.SQLXML::class,
        Types.STRUCT to Any::class,
        Types.TIME to java.time.LocalTime::class,
        Types.TIMESTAMP to java.time.LocalDateTime::class,
        Types.TIMESTAMP_WITH_TIMEZONE to java.time.LocalDateTime::class,
        Types.TIME_WITH_TIMEZONE to java.time.LocalDateTime::class,
//        Types.TINYINT to Byte::class,
        Types.TINYINT to Int::class,
        Types.VARBINARY to Array<Byte>::class,
        Types.VARCHAR to String::class
    )

    /**
     * 返回列的指定关系型数据库对应的Kotlin类型
     *
     * @param rdbType 关系型数据库枚举
     * @param column 列
     * @return Kotlin类型
     * @author K
     * @since 1.0.0
     */
    fun getKotlinType(rdbType: RdbType, column: Column): KClass<*> {
        val jdbcType = column.jdbcTypeName.toUpperCase()
        return when (rdbType) {
            RdbType.H2 -> {
                when (jdbcType) {
                    "BOOLEAN", "BIT", "BOOL" -> Boolean::class
                    "TINYINT" -> Int::class // Byte::class
                    "SMALLINT", "INT2", "YEAR" -> Int::class // Short::class
                    "INT", "INTEGER", "MEDIUMINT", "INT4", "SIGNED" -> Int::class
                    "BIGINT", "INT8", "IDENTITY" -> Long::class
                    "REAL" -> Float::class
                    "DOUBLE", "PRECISION", "FLOAT", "FLOAT4", "FLOAT8" -> Double::class
                    "DECIMAL", "NUMBER", "DEC", "NUMERI" -> java.math.BigDecimal::class
                    "VARCHAR", "LONGVARCHAR", "VARCHAR2", "NVARCHAR", "NVARCHAR2", "VARCHAR_CASESENSITIVE", "VARCHAR_IGNORECASE", "CHAR", "CHARACTER", "NCHAR" -> String::class
                    "BLOB", "TINYBLOB", "MEDIUMBLOB", "LONGBLOB", "IMAGE", "OID" -> java.sql.Blob::class // java.io.InputStream也支持
                    "CLOB", "TINYTEXT", "TEXT", "MEDIUMTEXT", "LONGTEXT", "NTEXT", "NCLOB" -> java.sql.Clob::class //java.io.Reader也支持
                    "DATE" -> java.time.LocalDate::class
                    "TIME" -> java.time.LocalTime::class
                    "TIMESTAMP", "DATETIME", "SMALLDATETIME" -> java.time.LocalDateTime::class
                    "BINARY", "VARBINARY", "LONGVARBINARY", "RAW", "BYTEA" -> Array<Byte>::class
                    "UUID" -> java.util.UUID::class
                    "ARRAY" -> Array<Any>::class
                    "OTHER" -> Any::class
                    else -> Any::class
                }
            }
            RdbType.MYSQL -> {
                when (jdbcType) {
                    "BIT" -> Boolean::class
                    "TINYINT", "SMALLINT", "MEDIUMINT", "BOOLEAN" -> Int::class
                    "INTEGER", "ID" -> Long::class
                    "FLOAT" -> Float::class
                    "DOUBLE" -> Double::class
                    "BIGINT" -> java.math.BigInteger::class
                    "DECIMAL" -> java.math.BigDecimal::class
                    "VARCHAR", "CHAR", "TEXT" -> String::class
                    "BLOB" -> Array<Byte>::class
                    "DATE", "YEAR" -> java.time.LocalDate::class
                    "TIME" -> java.time.LocalTime::class
                    "DATETIME", "TIMESTAMP" -> java.time.LocalDateTime::class
                    else -> Any::class
                }
            }
            RdbType.ORACLE -> {
                when (jdbcType) {
                    "BOOL", "BOOLEAN", "NUMBER(1)", "NUMBER(1,0)" -> Boolean::class
                    "NUMBER(2)", "NUMBER(2,0)" -> Int::class // Byte::class
                    "NUMBER(3)", "NUMBER(3,0)", "NUMBER(4)", "NUMBER(4,0)" -> Int::class // Short::class
                    "INTEGER", "INT", "SMALLINT", "NUMBER_INTEGER", "NUMBER(5)", "NUMBER(6)", "NUMBER(7)", "NUMBER(8)", "NUMBER(9)", "NUMBER(10)",
                    "NUMBER(5,0)", "NUMBER(6,0)", "NUMBER(7,0)", "NUMBER(8,0)", "NUMBER(9,0)", "NUMBER(10,0)" -> Int::class
                    "NUMBER_LONG", "NUMBER(11)", "NUMBER(12)", "NUMBER(13)", "NUMBER(14)", "NUMBER(15)", "NUMBER(16)", "NUMBER(17)", "NUMBER(18)", "NUMBER(19)",
                    "NUMBER(11,0)", "NUMBER(12,0)", "NUMBER(13,0)", "NUMBER(14,0)", "NUMBER(15,0)", "NUMBER(16,0)", "NUMBER(17,0)", "NUMBER(18,0)", "NUMBER(19,0)" -> Long::class
                    "FLOAT", "BINARY_FLOAT" -> Float::class
                    "DOUBLE", "BINARY_DOUBLE" -> Double::class
                    "NUMBER", "NUMBER(20)", "NUMBER(21)", "NUMBER(22)", "NUMBER(23)", "NUMBER(24)", "NUMBER(25)", "NUMBER(26)", "NUMBER(27)", "NUMBER(28)", "NUMBER(29)", "NUMBER(30)",
                    "NUMBER(31)", "NUMBER(32)", "NUMBER(33)", "NUMBER(34)", "NUMBER(35)", "NUMBER(36)", "NUMBER(37)", "NUMBER(38)", "NUMBER(38)" -> java.math.BigDecimal::class
                    "DEC", "DECIMAL", "DOUBLE PRECISION" -> java.math.BigDecimal::class
                    "VARCHAR2", "CHAR", "LONG", "NVARCHAR2", "CHARACTER", "VARCHAR", "NCLOB" -> String::class
                    "BFILE", "RAW", "LONGRAW", "LONG VARCHAR" -> Array<Byte>::class
                    "BLOB" -> java.sql.Blob::class
                    "CLOB", "NCLOB" -> java.sql.Clob::class
                    "DATE" -> java.time.LocalDate::class
                    "TIME" -> java.time.LocalTime::class
                    "DATETIME", "TIMESTAMP", "TIMESTAMP WITH TIME ZONE", "TIMESTAMP WITH LOCAL TIME ZONE", "INTERVAL", "INTERVAL YEAR TO MONTH", "INTERVAL DAY TO SECOND", "YEAR" -> java.time.LocalDateTime::class
                    "REF CURSOR" -> java.sql.ResultSet::class
                    else -> {
                        if (jdbcType.matches(Regex("""NUMBER\([1-8],\d+\)"""))) Float::class
                        if (jdbcType.matches(Regex("""NUMBER\(9|([1-9]\d),\d+\)"""))) Double::class
                        else Any::class
                    }
                }
            }
            RdbType.POSTGRESQL -> {
                when (jdbcType) {
                    "BIT", "BOOL" -> Boolean::class
                    "INT2", "INT4" -> Int::class
                    "INT8" -> Long::class
                    "FLOAT4" -> Float::class
                    "FLOAT8", "MONEY" -> Double::class
                    "NUMERIC" -> java.math.BigDecimal::class
                    "VARCHAR", "BPCHAR", "TEXT" -> String::class
                    "DATE" -> java.time.LocalDate::class
                    "TIME" -> java.time.LocalTime::class
                    "TIMESTAMP", "TIMESTAMP  WITHOUT TIMEZONE" -> java.time.LocalDateTime::class
                    "TIMESTAMP WITH TIMEZONE" -> java.time.OffsetDateTime::class
                    "BYTEA" -> Array<Byte>::class
                    "CIDR", "INET", "MACADDR", "BOX", "CIRCLE", "INTERVAL", "LINE", "LSEG", "PATH", "POINT", "POLYGON", "VARBIT" -> Any::class
                    else -> Any::class
                }
            }
            RdbType.SQLITE -> {
                when (jdbcType) {
                    "BOOLEAN" -> Boolean::class
                    "INT", "INT2", "INTEGER", "TINYINT", "SMALLINT", "MEDIUMINT" -> Int::class
                    "BIGINT", "UNSIGNED BIG INT", "INT8" -> Long::class
                    "FLOAT" -> Float::class
                    "REAL", "DOUBLE", "DOUBLE PRECISION", "NUMERIC" -> Double::class
                    "DECIMAL" -> java.math.BigDecimal::class
                    "CHARACTER", "VARCHAR", "VARYING CHARACTER", "NCHAR", "NATIVE CHARACTER", "NVARCHAR", "TEXT" -> String::class
                    "BLOB" -> java.sql.Blob::class
                    "CLOB" -> java.sql.Clob::class
                    "DATE" -> java.time.LocalDate::class
                    "DATETIME" -> java.time.LocalDateTime::class
                    else -> Any::class
                }
            }
            else -> {
                defaultMapping[column.jdbcType] ?: error("未支持JdbcType: ${column.jdbcType}")
            }
        }
    }

}