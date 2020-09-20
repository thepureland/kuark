package io.kuark.ability.data.rdb.metadata

import java.util.*
import kotlin.reflect.KClass

/**
 * kotlin类型和Ktorm sql类型函数名(用于dao中作列绑定)的映射
 *
 * @author K
 * @since 1.0.0
 */
object KtormSqlType {

    /**
     * 返回kotlin类型对应的Ktorm sql类型函数名
     *
     * @param clazz kotlin类型
     * @return Ktorm sql类型函数名
     * @author K
     * @since 1.0.0
     */
    fun getFunName(clazz: KClass<*>): String =
        when (clazz) {
            Boolean::class -> "boolean"
            Int::class -> "int"
            Long::class -> "long"
            Float::class -> "float"
            Double::class -> "double"
            java.math.BigDecimal::class -> "decimal"
            String::class -> "varchar"
            java.sql.Clob::class -> "text"
            java.sql.Blob::class -> "blob"
            ByteArray::class -> "bytes"
            java.sql.Timestamp::class -> "jdbcTimestamp"
            java.sql.Date::class -> "jdbcDate"
            java.sql.Time::class -> "jdbcTime"
            java.time.Instant::class -> "timestamp"
            java.time.LocalDateTime::class -> "datetime"
            java.time.LocalDate::class -> "date"
            java.time.LocalTime::class -> "time"
            java.time.MonthDay::class -> "monthDay"
            java.time.YearMonth::class -> "yearMonth"
            java.time.Year::class -> "year"
            Enum::class -> "enum"
            UUID::class -> "uuid"
            else -> "" //should not reach here
        }

}