package org.kuark.base.lang.string

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * StringKit单元测试类
 * @author K
 */
internal class StringKitTest {

    @Test
    fun testReplaceEach() {
        val map = mapOf("1" to "壹", "2" to "贰", "3" to "叁", null to "*", "" to "*")
        assertEquals("壹贰叁.壹", StringKit.replaceEach("123.1", map))
    }

    @Test
    fun testToHexStr() {
        assertEquals("4b6576696365", StringKit.toHexStr("Kevice"))
    }

    @Test
    fun testDecodeHexStr() {
        assertEquals("Kevice", StringKit.decodeHexStr("4b6576696365"))
    }

    @Test
    fun testDivideAverage() {
        assertEquals(0, StringKit.divideAverage(null, 3).size)
        assertEquals(0, StringKit.divideAverage("", 3).size)
        assertEquals(0, StringKit.divideAverage("ererr", 0).size)
        assertEquals(0, StringKit.divideAverage("ererr", -3).size)
        assertEquals(0, StringKit.divideAverage(null, 3).size)
        assertEquals(0, StringKit.divideAverage(null, 3).size)
        var arr = StringKit.divideAverage("123456", 3)
        assertEquals(3, arr.size)
        assertEquals("12", arr[0])
        assertEquals("34", arr[1])
        assertEquals("56", arr[2])
        arr = StringKit.divideAverage("1234567", 3)
        assertEquals(3, arr.size)
        assertEquals("123", arr[0])
        assertEquals("456", arr[1])
        assertEquals("7", arr[2])
    }

    @Test
    fun testHumpToUnderscore() {
        assertEquals("", StringKit.humpToUnderscore(null))
        assertEquals("", StringKit.humpToUnderscore(""))
        assertEquals("HUMP_TO_UNDERLINE", StringKit.humpToUnderscore("humpToUnderline"))
    }

    @Test
    fun testUnderscoreToHump() {
        assertEquals("", StringKit.underscoreToHump(null))
        assertEquals("", StringKit.underscoreToHump(""))
        assertEquals("humpToUnderline", StringKit.underscoreToHump("HUMP_TO_UNDERLINE"))
        assertFalse("HumpToUnderline" == StringKit.underscoreToHump("HUMP_TO_UNDERLINE"))
    }

    @Test
    fun testFillTemplate() {
        val tmpl = "\${param1} \${param2}$\${param1}"
        val paramMap = mapOf("param1" to "1", "param2" to "2")
        assertEquals(null, StringKit.fillTemplate(null, paramMap))
        assertEquals(tmpl, StringKit.fillTemplate(tmpl, null))
        assertEquals("1 2$1", StringKit.fillTemplate(tmpl, paramMap))
    }

    // ----------------------------------------------------------------------------
    // 封装org.apache.commons.lang3.StringUtils
    // ----------------------------------------------------------------------------
    // 判空
    // -----------------------------------------------------------------------
    @Test
    fun testIsEmpty() {
        assertTrue(StringKit.isEmpty(null))
        assertTrue(StringKit.isEmpty(""))
        assertFalse(StringKit.isEmpty("null"))
        assertFalse(StringKit.isEmpty(" "))
        assertFalse(StringKit.isEmpty("kevice"))
        assertFalse(StringKit.isEmpty(" kevice "))
    }

    @Test
    fun testIsNotEmpty() {
        assertFalse(StringKit.isNotEmpty(null))
        assertFalse(StringKit.isNotEmpty(""))
        assertTrue(StringKit.isNotEmpty("null"))
        assertTrue(StringKit.isNotEmpty(" "))
        assertTrue(StringKit.isNotEmpty("kevice"))
        assertTrue(StringKit.isNotEmpty(" kevice "))
    }

    @Test
    fun testIsBlank() {
        assertTrue(StringKit.isBlank(null))
        assertTrue(StringKit.isBlank(""))
        assertTrue(StringKit.isBlank(" "))
        assertTrue(StringKit.isBlank("\t\n\r\u000C"))
        assertFalse(StringKit.isBlank("null"))
        assertFalse(StringKit.isBlank("kevice"))
        assertFalse(StringKit.isBlank(" kevice "))
    }

    @Test
    fun testIsNotBlank() {
        assertFalse(StringKit.isNotBlank(null))
        assertFalse(StringKit.isNotBlank(""))
        assertFalse(StringKit.isNotBlank(" "))
        assertFalse(StringKit.isNotBlank("\t\n\r\u000C"))
        assertTrue(StringKit.isNotBlank("null"))
        assertTrue(StringKit.isNotBlank("kevice"))
        assertTrue(StringKit.isNotBlank(" kevice "))
    }

    // Trim
    // -----------------------------------------------------------------------
    @Test
    fun testTrim() {
        assertEquals(null, StringKit.trim(null))
        assertEquals("", StringKit.trim(""))
        assertEquals("", StringKit.trim(" "))
        assertEquals("", StringKit.trim("\b\t\n\r\u000C"))
        assertEquals("null", StringKit.trim("null"))
        assertEquals("kevice", StringKit.trim("kevice"))
        assertEquals("k e v i c e", StringKit.trim(" k e v i c e "))
    }

    @Test
    fun testTrimToNull() {
        assertEquals(null, StringKit.trimToNull(null))
        assertEquals(null, StringKit.trimToNull(""))
        assertEquals(null, StringKit.trimToNull(" "))
        assertEquals(null, StringKit.trimToNull("\b\t\n\r\u000C"))
        assertEquals("null", StringKit.trimToNull("null"))
        assertEquals("kevice", StringKit.trimToNull("kevice"))
        assertEquals("k e v i c e", StringKit.trimToNull(" k e v i c e "))
    }

    @Test
    fun testTrimToEmpty() {
        assertEquals("", StringKit.trimToEmpty(null))
        assertEquals("", StringKit.trimToEmpty(""))
        assertEquals("", StringKit.trimToEmpty(" "))
        assertEquals("", StringKit.trimToEmpty("\b\t\n\r\u000C"))
        assertEquals("null", StringKit.trimToEmpty("null"))
        assertEquals("kevice", StringKit.trimToEmpty("kevice"))
        assertEquals("k e v i c e", StringKit.trimToEmpty(" k e v i c e "))
    }

    // Stripping
    // -----------------------------------------------------------------------
    @Test
    fun testStrip() {
        assertEquals(null, StringKit.strip(null))
        assertEquals("", StringKit.strip(""))
        assertEquals("", StringKit.strip(" "))
        assertEquals("\b", StringKit.strip("\b\t\n\r\u000C")) // 注意和trim的差别
        assertEquals("null", StringKit.strip("null"))
        assertEquals("kevice", StringKit.strip("kevice"))
        assertEquals("k e v i c e", StringKit.strip(" k e v i c e "))
    }

    @Test
    fun testStripToNull() {
        assertEquals(null, StringKit.stripToNull(null))
        assertEquals(null, StringKit.stripToNull(""))
        assertEquals(null, StringKit.stripToNull(" "))
        assertEquals("\b", StringKit.stripToNull("\b\t\n\r\u000C"))
        assertEquals("null", StringKit.stripToNull("null"))
        assertEquals("kevice", StringKit.stripToNull("kevice"))
        assertEquals("k e v i c e", StringKit.stripToNull(" k e v i c e "))
    }

    @Test
    fun testStripToEmpty() {
        assertEquals("", StringKit.stripToEmpty(null))
        assertEquals("", StringKit.stripToEmpty(""))
        assertEquals("", StringKit.stripToEmpty(" "))
        assertEquals("\b", StringKit.stripToEmpty("\b\t\n\r\u000C"))
        assertEquals("null", StringKit.stripToEmpty("null"))
        assertEquals("kevice", StringKit.stripToEmpty("kevice"))
        assertEquals("k e v i c e", StringKit.stripToEmpty(" k e v i c e "))
    }

    @Test
    fun testStripByStr() {
        assertEquals(null, StringKit.strip(null, "*"))
        assertEquals("k", StringKit.strip(" k ", null))
        assertEquals("\b", StringKit.strip("\b\t\n\r\u000C", null))
        assertEquals("null", StringKit.strip("null", "*"))
        assertEquals("  abc", StringKit.strip("yxzxy  abcyx", "xyz"))
    }

    @Test
    fun testStripStartByStr() {
        assertEquals(null, StringKit.stripStart(null, "*"))
        assertEquals("k ", StringKit.stripStart(" k ", null))
        assertEquals("\b", StringKit.stripStart("\t\n\r\u000C\b", null))
        assertEquals("null", StringKit.stripStart("null", "*"))
        assertEquals("  abcyx", StringKit.stripStart("yxzxy  abcyx", "xyz"))
    }

    @Test
    fun testStripEndByStr() {
        assertEquals(null, StringKit.stripEnd(null, "*"))
        assertEquals(" k", StringKit.stripEnd(" k ", null))
        assertEquals("\b", StringKit.stripEnd("\b\t\n\r\u000C", null))
        assertEquals("null", StringKit.stripEnd("null", "*"))
        assertEquals("yxzxy  abc", StringKit.stripEnd("yxzxy  abcyx", "xyz"))
        assertEquals("12", StringKit.stripEnd("120.00", ".0"))
    }

    @Test
    fun testStripAll() {
        val strs = arrayOf(null, "", " ", "\b\t\n\r\u000C", "null", "kevice", " k e v i c e ")
        val results = StringKit.stripAll(*strs)
        assertEquals(null, results[0])
        assertEquals("", results[1])
        assertEquals("", results[2])
        assertEquals("\b", results[3])
        assertEquals("null", results[4])
        assertEquals("kevice", results[5])
        assertEquals("k e v i c e", results[6])
    }

    // @Test
    // public void testStripAccents() {
    // assertEquals(null, StringUtils.stripAccents(null));
    // assertEquals("", StringUtils.stripAccents(""));
    // assertEquals("control", StringUtils.stripAccents("control"));
    // assertEquals("eclair", StringUtils.stripAccents("&eacute;clair"));
    // assertEquals("a", StringUtils.stripAccents("&agrave;"));
    // }
    // Equals
    // -----------------------------------------------------------------------
    @Test
    fun testEquals() {
        assertTrue(StringKit.equals(null, null))
        assertTrue(StringKit.equals("abc", "abc"))
        assertFalse(StringKit.equals(null, "abc"))
        assertFalse(StringKit.equals("abc", null))
        assertFalse(StringKit.equals("abc", "ABC"))
    }

    @Test
    fun testEqualsIgnoreCase() {
        assertTrue(StringKit.equalsIgnoreCase(null, null))
        assertTrue(StringKit.equalsIgnoreCase("abc", "abc"))
        assertTrue(StringKit.equalsIgnoreCase("abc", "ABC"))
        assertFalse(StringKit.equalsIgnoreCase(null, "abc"))
        assertFalse(StringKit.equalsIgnoreCase("abc", null))
    }

    // IndexOf
    // -----------------------------------------------------------------------
    @Test
    fun testIndexOf() {
        assertEquals(-1, StringKit.indexOf(null, "*"))
        assertEquals(-1, StringKit.indexOf("", "*"))
        assertEquals(0, StringKit.indexOf("aabaabaa", "a"))
        assertEquals(2, StringKit.indexOf("aabaabaa", "b"))
    }

    @Test
    fun testIndexOfFrom() {
        assertEquals(-1, StringKit.indexOf(null, "*", 1))
        assertEquals(-1, StringKit.indexOf("", "*", 1))
        assertEquals(2, StringKit.indexOf("aabaabaa", "b", 0))
        assertEquals(5, StringKit.indexOf("aabaabaa", "b", 3))
        assertEquals(-1, StringKit.indexOf("aabaabaa", "b", 9))
        assertEquals(2, StringKit.indexOf("aabaabaa", "b", -1))
    }

    @Test
    fun testIndexOfString() {
        assertEquals(-1, StringKit.indexOf(null, "*"))
        assertEquals(-1, StringKit.indexOf("*", null))
        assertEquals(-1, StringKit.indexOf("", "*"))
        assertEquals(0, StringKit.indexOf("", ""))
        assertEquals(0, StringKit.indexOf("aabaabaa", "a"))
        assertEquals(2, StringKit.indexOf("aabaabaa", "b"))
        assertEquals(1, StringKit.indexOf("aabaabaa", "ab"))
        assertEquals(0, StringKit.indexOf("aabaabaa", ""))
    }

    @Test
    fun testIndexOfStringFrom() {
        assertEquals(-1, StringKit.indexOf(null, "*", 1))
        assertEquals(-1, StringKit.indexOf("*", null, 1))
        assertEquals(-1, StringKit.indexOf("", "*", 2))
        assertEquals(0, StringKit.indexOf("", "", 0))
        assertEquals(1, StringKit.indexOf("aabaabaa", "a", 1))
        assertEquals(5, StringKit.indexOf("aabaabaa", "b", 4))
        assertEquals(1, StringKit.indexOf("aabaabaa", "ab", 0))
        assertEquals(0, StringKit.indexOf("aabaabaa", "", -1))
    }

    @Test
    fun testOrdinalIndexOf() {
        assertEquals(-1, StringKit.ordinalIndexOf(null, "*", 1))
        assertEquals(-1, StringKit.ordinalIndexOf("*", null, 1))
        assertEquals(-1, StringKit.ordinalIndexOf("", "*", 2))
        assertEquals(0, StringKit.ordinalIndexOf("", "", 2))
        assertEquals(0, StringKit.ordinalIndexOf("aabaabaa", "a", 1))
        assertEquals(5, StringKit.ordinalIndexOf("aabaabaa", "b", 2))
        assertEquals(4, StringKit.ordinalIndexOf("aabaabaa", "ab", 2))
        assertEquals(0, StringKit.ordinalIndexOf("aabaabaa", "", 2))
    }

    @Test
    fun testIndexOfIgnoreCase() {
        assertEquals(-1, StringKit.indexOfIgnoreCase(null, "*"))
        assertEquals(-1, StringKit.indexOfIgnoreCase("", "*"))
        assertEquals(0, StringKit.indexOfIgnoreCase("", ""))
        assertEquals(0, StringKit.indexOfIgnoreCase("aabaabaa", "a"))
        assertEquals(2, StringKit.indexOfIgnoreCase("aabaaBaa", "B"))
        assertEquals(1, StringKit.indexOfIgnoreCase("aabaAbaa", "aB"))
    }

    @Test
    fun testIndexOfIgnoreCaseFrom() {
        assertEquals(-1, StringKit.indexOfIgnoreCase(null, "*", 1))
        assertEquals(-1, StringKit.indexOfIgnoreCase("*", null, 1))
        assertEquals(-1, StringKit.indexOfIgnoreCase("", "*", 2))
        assertEquals(0, StringKit.indexOfIgnoreCase("", "", 0))
        assertEquals(1, StringKit.indexOfIgnoreCase("aabaabaa", "A", 1))
        assertEquals(5, StringKit.indexOfIgnoreCase("aaBaaBaa", "B", 4))
        assertEquals(1, StringKit.indexOfIgnoreCase("aabaabaa", "Ab", 0))
        assertEquals(0, StringKit.indexOfIgnoreCase("aabaabaa", "", -1))
    }

    @Test
    fun testLastIndexOf() {
        assertEquals(-1, StringKit.lastIndexOf(null, "*"))
        assertEquals(-1, StringKit.lastIndexOf("", "*"))
        assertEquals(7, StringKit.lastIndexOf("aabaabaa", "a"))
        assertEquals(5, StringKit.lastIndexOf("aabaabaa", "b"))
    }

    @Test
    fun testLastIndexOfFrom() {
        assertEquals(-1, StringKit.lastIndexOf(null, "*", 0))
        assertEquals(-1, StringKit.lastIndexOf("", "*", 0))
        assertEquals(5, StringKit.lastIndexOf("aabaabaa", "b", 8))
        assertEquals(2, StringKit.lastIndexOf("aabaabaa", "b", 4))
        assertEquals(-1, StringKit.lastIndexOf("aabaabaa", "b", 0))
        assertEquals(5, StringKit.lastIndexOf("aabaabaa", "b", 9))
        assertEquals(-1, StringKit.lastIndexOf("aabaabaa", "b", -1))
        assertEquals(0, StringKit.lastIndexOf("aabaabaa", "a", 0))
    }

    @Test
    fun testLastIndexOfString() {
        assertEquals(-1, StringKit.lastIndexOf(null, "*"))
        assertEquals(-1, StringKit.lastIndexOf("*", null))
        assertEquals(0, StringKit.lastIndexOf("", ""))
        assertEquals(7, StringKit.lastIndexOf("aabaabaa", "a"))
        assertEquals(5, StringKit.lastIndexOf("aabaabaa", "b"))
        assertEquals(4, StringKit.lastIndexOf("aabaabaa", "ab"))
        assertEquals(8, StringKit.lastIndexOf("aabaabaa", ""))
    }

    @Test
    fun testLastOrdinalIndexOf() {
        assertEquals(-1, StringKit.lastOrdinalIndexOf(null, "*", 8))
        assertEquals(-1, StringKit.lastOrdinalIndexOf("*", null, 8))
        assertEquals(0, StringKit.lastOrdinalIndexOf("", "", 8))
        assertEquals(7, StringKit.lastOrdinalIndexOf("aabaabaa", "a", 1))
        assertEquals(6, StringKit.lastOrdinalIndexOf("aabaabaa", "a", 2))
        assertEquals(5, StringKit.lastOrdinalIndexOf("aabaabaa", "b", 1))
        assertEquals(2, StringKit.lastOrdinalIndexOf("aabaabaa", "b", 2))
        assertEquals(4, StringKit.lastOrdinalIndexOf("aabaabaa", "ab", 1))
        assertEquals(1, StringKit.lastOrdinalIndexOf("aabaabaa", "ab", 2))
        assertEquals(8, StringKit.lastOrdinalIndexOf("aabaabaa", "", 1))
        assertEquals(8, StringKit.lastOrdinalIndexOf("aabaabaa", "", 2))
    }

    @Test
    fun testLastIndexOfStringFrom() {
        assertEquals(-1, StringKit.lastIndexOf(null, "*", 8))
        assertEquals(-1, StringKit.lastIndexOf("*", null, 8))
        assertEquals(7, StringKit.lastIndexOf("aabaabaa", "a", 8))
        assertEquals(5, StringKit.lastIndexOf("aabaabaa", "b", 8))
        assertEquals(4, StringKit.lastIndexOf("aabaabaa", "ab", 8))
        assertEquals(5, StringKit.lastIndexOf("aabaabaa", "b", 9))
        assertEquals(-1, StringKit.lastIndexOf("aabaabaa", "b", -1))
        assertEquals(0, StringKit.lastIndexOf("aabaabaa", "a", 0))
        assertEquals(-1, StringKit.lastIndexOf("aabaabaa", "b", 0))
    }

    @Test
    fun testlastIndexOfIgnoreCase() {
        assertEquals(-1, StringKit.lastIndexOfIgnoreCase(null, "*"))
        assertEquals(-1, StringKit.lastIndexOfIgnoreCase("*", null))
        assertEquals(7, StringKit.lastIndexOfIgnoreCase("aabaabaa", "A"))
        assertEquals(5, StringKit.lastIndexOfIgnoreCase("aabaabaa", "B"))
        assertEquals(4, StringKit.lastIndexOfIgnoreCase("aabaabaa", "AB"))
    }

    @Test
    fun testLastIndexOfIgnoreCase() {
        assertEquals(-1, StringKit.lastIndexOfIgnoreCase(null, "*", 8))
        assertEquals(-1, StringKit.lastIndexOfIgnoreCase("*", null, 8))
        assertEquals(7, StringKit.lastIndexOfIgnoreCase("aabaabaa", "A", 8))
        assertEquals(5, StringKit.lastIndexOfIgnoreCase("aabaabaa", "B", 8))
        assertEquals(4, StringKit.lastIndexOfIgnoreCase("aabaabaa", "AB", 8))
        assertEquals(5, StringKit.lastIndexOfIgnoreCase("aabaabaa", "B", 9))
        assertEquals(-1, StringKit.lastIndexOfIgnoreCase("aabaabaa", "B", -1))
        assertEquals(0, StringKit.lastIndexOfIgnoreCase("aabaabaa", "A", 0))
        assertEquals(-1, StringKit.lastIndexOfIgnoreCase("aabaabaa", "B", 0))
    }

    // Contains
    // -----------------------------------------------------------------------
    @Test
    fun testContains() {
        assertFalse(StringKit.contains(null, "*"))
        assertFalse(StringKit.contains("", "*"))
        assertTrue(StringKit.contains("abc", "a"))
        assertFalse(StringKit.contains("abc", "z"))
    }
}