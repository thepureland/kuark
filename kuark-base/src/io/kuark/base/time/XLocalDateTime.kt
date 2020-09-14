package io.kuark.base.time

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * 定义 java.time.LocalDateTime 的扩展函数
 *
 * @author K
 * @since 1.0.0
 */


/**
 * 格式化
 *
 * @param pattern 格式模式串，常见的可用DateTimeFormatPattern类中的常量
 * @return 格式化后的字符串
 * @author K
 * @since 1.0.0
 */
fun LocalDateTime.format(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))

/**
 * 返回当前时间对应的quartz的cron表达式
 *
 * @return quartz的cron表达式
 * @author K
 * @since 1.0.0
 */
fun LocalDateTime.toCronExp(): String =
    with(this) {
        "$second $minute $hour $dayOfMonth ${month.value} ? $year"
    }