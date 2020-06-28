package org.kuark.base.collections

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class ArrayKitTest {
    
    @Test
    fun testMapToArrOfArr() {
        val map = mapOf("k1" to "v1", "k2" to "v2", "k3" to "v3")
        val arrOfArr = ArrayKit.mapToArrOfArr(map)
        assertEquals(3, arrOfArr.size.toLong())
        assertEquals("k1", arrOfArr[0][0])
        assertEquals("v1", arrOfArr[0][1])
        assertEquals("k2", arrOfArr[1][0])
        assertEquals("v2", arrOfArr[1][1])
        assertEquals("k3", arrOfArr[2][0])
        assertEquals("v3", arrOfArr[2][1])
    }

    @Test
    fun testStrArrToNumArr() {
        var strs = arrayOf("0", "1", "-1")
        val bytes: Array<Byte> = ArrayKit.strArrToNumArr(strs, Byte::class)
        assertEquals(java.lang.Byte.valueOf("0"), bytes[0])
        assertEquals(java.lang.Byte.valueOf("1"), bytes[1])
        assertEquals(java.lang.Byte.valueOf("-1"), bytes[2])
        val shorts: Array<Short> = ArrayKit.strArrToNumArr(strs, Short::class)
        assertEquals("0".toShort(), shorts[0])
        val integers: Array<Int> = ArrayKit.strArrToNumArr(strs, Int::class)
        assertEquals(Integer.valueOf("0"), integers[0])
        val longs: Array<Long> = ArrayKit.strArrToNumArr(strs, Long::class)
        assertEquals(java.lang.Long.valueOf("0"), longs[0])
        val dbls: Array<Double> = ArrayKit.strArrToNumArr(strs, Double::class)
        assertEquals(java.lang.Double.valueOf("-1.0"), dbls[2])
        strs = arrayOf("0.0", "1.0", "-1.1")
        val floats: Array<Float> = ArrayKit.strArrToNumArr(strs, Float::class)
        assertEquals(java.lang.Float.valueOf("0.0"), floats[0])
        assertEquals(java.lang.Float.valueOf("1.0"), floats[1])
        assertEquals(java.lang.Float.valueOf("-1.1"), floats[2])
        val doubles: Array<Double> = ArrayKit.strArrToNumArr(strs, Double::class)
        assertEquals(java.lang.Double.valueOf("-1.1"), doubles[2])
        try {
            ArrayKit.strArrToNumArr(strs, Int::class)
        } catch (e: Exception) {
            assertTrue(true)
        }
        try {
            ArrayKit.strArrToNumArr(strs, BigDecimal::class)
        } catch (e: Exception) {
            assertTrue(true)
        }
    }
    
}