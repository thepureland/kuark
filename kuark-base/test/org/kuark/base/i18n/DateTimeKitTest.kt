package org.kuark.base.i18n

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

/**
 * DateTimeKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class DateTimeKitTest {

    private lateinit var date: Date
    private lateinit var cal: Calendar

    @BeforeEach
    fun setUp() {
        cal = Calendar.getInstance()
        with(cal) {
            set(Calendar.YEAR, 2010)
            set(Calendar.MONTH, 7)
            set(Calendar.DATE, 23)
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 21)
            set(Calendar.SECOND, 21)
            set(Calendar.MILLISECOND, 111)
        }
        date = cal.time
    }


    @Test
    fun testSwapDates() {
        val date1 = (cal!!.clone() as Calendar).time
        val cal2 = cal!!.clone() as Calendar
        cal2.add(Calendar.MILLISECOND, 100)
        val date2 = cal2.time
        DateTimeKit.swapDates(date1, date2)
        assertEquals(date1.time, date2.time + 100)
    }

    @Test
    fun testGetActualAge() {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.YEAR, 2)
        assertEquals(2, DateTimeKit.getActualAge(date, cal.time))
        assertTrue(DateTimeKit.getActualAge(date, null) >= 2)
    }

    @Test
    fun testDaysBetween() {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, 1)
        cal.add(Calendar.MILLISECOND, 1)
        assertEquals(1, DateTimeKit.daysBetween(cal.time, date))
        cal.add(Calendar.MILLISECOND, -2)
        assertEquals(0, DateTimeKit.daysBetween(cal.time, date))
    }

    @Test
    fun testToCronExp() {
        assertEquals("21 21 21 23 8 ? 2010", DateTimeKit.toCronExp(date))
    }

    @Test
    fun testTimeFieldOfSeconds() {
        var seconds: Int =
            DateTimeKit.SECONDS_OF_YEAR + DateTimeKit.SECONDS_OF_MONTH + DateTimeKit.SECONDS_OF_DAY + DateTimeKit.SECONDS_OF_HOUR + DateTimeKit.SECONDS_OF_MINUTE + 1
        var map = DateTimeKit.timeFieldOfSeconds(seconds.toLong())
        for (value in map.values) {
            assertEquals(1L, value.toLong())
        }
        seconds = DateTimeKit.SECONDS_OF_HOUR + DateTimeKit.SECONDS_OF_MINUTE + 1
        map = DateTimeKit.timeFieldOfSeconds(seconds.toLong())
        assertEquals(null, map[Calendar.YEAR])
        assertEquals(null, map[Calendar.MONTH])
        assertEquals(null, map[Calendar.DAY_OF_YEAR])
        assertEquals(1L, map[Calendar.HOUR]!!.toLong())
        assertEquals(1L, map[Calendar.MINUTE]!!.toLong())
        assertEquals(1L, map[Calendar.SECOND]!!.toLong())
    }

    @Test
    fun test_formatDate() {
        val now = Date()
        var date1: String =
            DateTimeKit.formatDate(now, TimeZone.getTimeZone("GMT+08:00"), DateTimeKit.yyyy_MM_dd_HH_mm_ss)
        var date2: String =
            DateTimeKit.formatDate(now, TimeZone.getTimeZone("GMT+08:00"), DateTimeKit.yyyy_MM_dd_HH_mm_ss)
        assertEquals(date1, date2)
        date1 = DateTimeKit.formatDate(
            now,
            LocaleKit.getLocale("zh_CN")!!,
            TimeZone.getTimeZone("GMT+08:00"),
            DateTimeKit.yyyy_MM_dd_HH_mm_ss
        )
        date2 = DateTimeKit.formatDate(
            now,
            LocaleKit.getLocale("en_US")!!,
            TimeZone.getTimeZone("GMT+08:00"),
            DateTimeKit.yyyy_MM_dd_HH_mm_ss
        )
        assertEquals(date1, date2)
    }

    @Test
    fun test_change_format() {
        val now = Date()
        with(DateTimeKit) {
            assertEquals(formatDate(now, yyyyMMdd), formatDate(now, yyyyMMdd))
            assertEquals(formatDate(now, yyyyMMddHHmmssSSS), formatDate(now, yyyyMMddHHmmssSSS))
            assertEquals(formatDate(now, yyyyMMddHHmmss), formatDate(now, yyyyMMddHHmmss))
            assertEquals(formatDate(now, yyyy_MM_dd), formatDate(now, yyyy_MM_dd))
            assertEquals(formatDate(now, yyyy_MM_dd_HH_mm_ss), formatDate(now, yyyy_MM_dd_HH_mm_ss))
            assertEquals(formatDate(now, MM_dd_HH_mm_ss), formatDate(now, MM_dd_HH_mm_ss))
            assertEquals(formatDate(now, HH_mm_ss), formatDate(now, HH_mm_ss))
        }

    }

}