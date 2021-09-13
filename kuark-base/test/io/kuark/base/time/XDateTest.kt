package io.kuark.base.time

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

/**
 * XDate单元测试类
 *
 * @author K
 * @since 1.0.0
 */
internal class XDateTest {

    private var time: Date

    private val YEAR = 2021
    private val MONTH = 9
    private val DAY = 10
    private val HOUR = 9
    private val MIN = 45
    private val SECOND = 1

    init {
        val calendar = Calendar.getInstance()
        calendar.set(YEAR, MONTH, DAY, HOUR, MIN, SECOND)
        time = calendar.time
    }

    @Test
    fun toLocalDateTime() {
        val localDateTime = time.toLocalDateTime()
        assertEquals(YEAR, localDateTime.year)
        assertEquals(MONTH + 1, localDateTime.month.value)
        assertEquals(DAY, localDateTime.dayOfMonth)
        assertEquals(HOUR, localDateTime.hour)
        assertEquals(MIN, localDateTime.minute)
        assertEquals(SECOND, localDateTime.second)
    }

    @Test
    fun toLocalDate() {
        val localDate = time.toLocalDate()
        assertEquals(YEAR, localDate.year)
        assertEquals(MONTH + 1, localDate.month.value)
        assertEquals(DAY, localDate.dayOfMonth)
    }

    @Test
    fun toLocalTime() {
        val localTime = time.toLocalTime()
        assertEquals(HOUR, localTime.hour)
        assertEquals(MIN, localTime.minute)
        assertEquals(SECOND, localTime.second)
    }

}