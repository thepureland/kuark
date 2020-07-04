package org.kuark.base.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

/**
 * EnumKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class EnumKitTest {

    private val enumClass: KClass<TimeUnit> = TimeUnit::class
    private val enumClassStr: String = TimeUnit::class.java.name

    @Test
    fun enumOf() {
        var code = "9"
        assertEquals(TimeUnit.MICROSECOND, EnumKit.enumOf(enumClass, code))
        assertNull(EnumKit.enumOf(YesNot::class, code))
        code = "would not find"
        assertNull(EnumKit.enumOf(enumClass, code))
    }

    @Test
    fun enumOfStr() {
        var code = "1"
        assertEquals(TimeUnit.YEAR, EnumKit.enumOf(enumClassStr, code))
        try {
            EnumKit.enumOf("err.class", code)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
        code = "would not find"
        assertNull(EnumKit.enumOf(enumClassStr, code))
        assertNull(EnumKit.enumOf(YesNot::class.java.getName(), code))
    }

    @Test
    fun getCodeMap() {
        val codeMap: Map<String, String?> = EnumKit.getCodeMap(enumClass)
        assertTrue(codeMap.size >= 9)
        assertEquals(TimeUnit.YEAR.trans, codeMap["1"])
        assertEquals(TimeUnit.MICROSECOND.trans, codeMap["9"])
    }

    @Test
    fun getCodeMapStr() {
        val codeMap: Map<String, String?> = EnumKit.getCodeMap(enumClassStr)
        //		Assert.assertTrue(codeMap.size() >= 9);
        assertEquals(TimeUnit.YEAR.trans, codeMap["1"])
        assertEquals(TimeUnit.MICROSECOND.trans, codeMap["9"])
        try {
            EnumKit.getCodeMap("")
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
        try {
            EnumKit.getCodeMap("err.class")
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
        try {
            EnumKit.getCodeMap(javaClass.name)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun getCodeEnumClass() {
        val codeEnumClass = EnumKit.getCodeEnumClass(enumClassStr)
        assertTrue(codeEnumClass == enumClass)
        try {
            EnumKit.getCodeEnumClass("")
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
        try {
            EnumKit.getCodeEnumClass("err.class")
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
        try {
            EnumKit.getCodeEnumClass(javaClass.name)
            assertTrue(false)
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    internal enum class TimeUnit(override val code: String, override var trans: String) : ICodeEnum {
        YEAR("1", "年"),
        MONTH("2", "月"),
        WEEK("3", "周"),
        DAY("4", "日"),
        HOUR("5", "小时"),
        MINUTE("6", "分钟"),
        SECOND("7", "秒"),
        MILLISECOND("8", "毫秒"),
        MICROSECOND("9", "微秒");

        fun intValue(): Int {
            return Integer.valueOf(code)
        }

        companion object {
            const val CODE_TABLE_ID = "time_unit"
            fun initTrans(map: Map<String?, String?>) {
                val values: Array<TimeUnit> = TimeUnit.values()
                for (timeUnit in values) {
                    timeUnit.trans = map[timeUnit.code]!!
                }
            }

            fun enumOf(code: String): TimeUnit? {
                return EnumKit.enumOf(TimeUnit::class, code)
            }
        }

    }

}