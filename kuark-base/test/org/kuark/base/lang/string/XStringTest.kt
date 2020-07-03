package org.kuark.base.lang.string

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * XString单元测试类
 * @author K
 */
internal class XStringTest {

    @Test
    fun testReplaceEach() {
        val map = mapOf("1" to "壹", "2" to "贰", "3" to "叁", null to "*", "" to "*")
        assertEquals("壹贰叁.壹", "123.1".replaceEach(map))
    }

    @Test
    fun testToHexStr() {
        assertEquals("4b6576696365", "Kuark".toHexStr())
    }

    @Test
    fun testDecodeHexStr() {
        assertEquals("Kuark", "4b6576696365".decodeHexStr())
    }

    @Test
    fun testDivideAverage() {
        assertEquals(0, "".divideAverage(3).size)
        assertEquals(0, "ererr".divideAverage(0).size)
        assertEquals(0, "ererr".divideAverage(-3).size)
        var arr = "123456".divideAverage(3)
        assertEquals(3, arr.size)
        assertEquals("12", arr[0])
        assertEquals("34", arr[1])
        assertEquals("56", arr[2])
        arr = "1234567".divideAverage(3)
        assertEquals(3, arr.size)
        assertEquals("123", arr[0])
        assertEquals("456", arr[1])
        assertEquals("7", arr[2])
    }

    @Test
    fun testHumpToUnderscore() {
        assertEquals("", "".humpToUnderscore())
        assertEquals("HUMP_TO_UNDERLINE", "humpToUnderline".humpToUnderscore())
    }

    @Test
    fun testUnderscoreToHump() {
        assertEquals("", "".underscoreToHump())
        assertEquals("humpToUnderline", "HUMP_TO_UNDERLINE".underscoreToHump())
        assertFalse("HumpToUnderline" == "HUMP_TO_UNDERLINE".underscoreToHump())
    }

    @Test
    fun testFillTemplate() {
        val tmpl = "\${param1} \${param2}$\${param1}"
        val paramMap = mapOf("param1" to "1", "param2" to "2")
        assertEquals("1 2$1", tmpl.fillTemplateByObjectMap(paramMap))
    }

}