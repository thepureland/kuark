package io.kuark.base.time

import java.time.*
import java.util.*


/**
 * 定义 java.util.Date 的扩展函数
 *
 * @author K
 * @since 1.0.0
 */

/**
 * 转换为LocalDateTime
 *
 * @param zoneId 时区ID，缺省为系统默认时区
 * @return LocalDateTime
 * @author K
 * @since 1.0.0
 */
fun Date.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime =
    toZonedDateTime(this, zoneId).toLocalDateTime()

/**
 * 转换为LocalDate
 *
 * @param zoneId 时区ID，缺省为系统默认时区
 * @return LocalDate
 * @author K
 * @since 1.0.0
 */
fun Date.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate = toZonedDateTime(this, zoneId).toLocalDate()

/**
 * 转换为LocalTime
 *
 * @param zoneId 时区ID，缺省为系统默认时区
 * @return LocalTime
 * @author K
 * @since 1.0.0
 */
fun Date.toLocalTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalTime = toZonedDateTime(this, zoneId).toLocalTime()

private fun toZonedDateTime(date: Date, zoneId: ZoneId): ZonedDateTime {
    val instant = date.toInstant()
    return instant.atZone(zoneId)
}