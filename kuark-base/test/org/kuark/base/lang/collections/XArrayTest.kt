package org.kuark.base.lang.collections

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * XArray测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class XArrayTest {
    
    @Test
    fun testToNumberArray() {
        var strs = arrayOf("0", "1", "-1")
        val bytes: Array<Byte> = strs.toNumberArray(Byte::class)
        assertEquals(java.lang.Byte.valueOf("0"), bytes[0])
        assertEquals(java.lang.Byte.valueOf("1"), bytes[1])
        assertEquals(java.lang.Byte.valueOf("-1"), bytes[2])
        val shorts: Array<Short> = strs.toNumberArray(Short::class)
        assertEquals("0".toShort(), shorts[0])
        val integers: Array<Int> = strs.toNumberArray(Int::class)
        assertEquals(Integer.valueOf("0"), integers[0])
        val longs: Array<Long> = strs.toNumberArray(Long::class)
        assertEquals(java.lang.Long.valueOf("0"), longs[0])
        val dbls: Array<Double> = strs.toNumberArray(Double::class)
        assertEquals(java.lang.Double.valueOf("-1.0"), dbls[2])
        strs = arrayOf("0.0", "1.0", "-1.1")
        val floats: Array<Float> = strs.toNumberArray(Float::class)
        assertEquals(java.lang.Float.valueOf("0.0"), floats[0])
        assertEquals(java.lang.Float.valueOf("1.0"), floats[1])
        assertEquals(java.lang.Float.valueOf("-1.1"), floats[2])
        val doubles: Array<Double> = strs.toNumberArray(Double::class)
        assertEquals(java.lang.Double.valueOf("-1.1"), doubles[2])
        try {
            strs.toNumberArray(Int::class)
        } catch (e: Exception) {
            assertTrue(true)
        }
        try {
            strs.toNumberArray(BigDecimal::class)
        } catch (e: Exception) {
            assertTrue(true)
        }
    }
    
}