package io.kuark.base.time

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 定义 java.time.LocalDate 的扩展函数
 *
 * @author K
 * @since 1.0.0
 */


/**
 * 格式化
 *
 * @param pattern 格式模式串，常见的可用DateTimeFormatPattern类中的常量
 * @return 格式化后的字符串
 */
fun LocalDate.format(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))

