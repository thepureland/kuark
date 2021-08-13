package io.kuark.base.lang.collections

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ArrayKitTest {

    @Test
    fun isEmpty() {
        assert(ArrayKit.isEmpty(null))
        assert(ArrayKit.isEmpty(emptyArray<Int>()))
        assertFalse(ArrayKit.isEmpty(arrayOf(1, 2)))
    }

    @Test
    fun isByteArrayEmpty() {
        assert(ArrayKit.isByteArrayEmpty(null))
        assert(ArrayKit.isByteArrayEmpty(byteArrayOf()))
        assertFalse(ArrayKit.isByteArrayEmpty(byteArrayOf(1, 2)))
    }

    @Test
    fun isCharArrayEmpty() {
        assert(ArrayKit.isCharArrayEmpty(null))
        assert(ArrayKit.isCharArrayEmpty(charArrayOf()))
        assertFalse(ArrayKit.isCharArrayEmpty(charArrayOf('a', 'b')))
    }

    @Test
    fun isShortArrayEmpty() {
        assert(ArrayKit.isShortArrayEmpty(null))
        assert(ArrayKit.isShortArrayEmpty(shortArrayOf()))
        assertFalse(ArrayKit.isShortArrayEmpty(shortArrayOf(1, 2)))
    }

    @Test
    fun isIntArrayEmpty() {
        assert(ArrayKit.isIntArrayEmpty(null))
        assert(ArrayKit.isIntArrayEmpty(intArrayOf()))
        assertFalse(ArrayKit.isIntArrayEmpty(intArrayOf(1, 2)))
    }

    @Test
    fun isLongArrayEmpty() {
        assert(ArrayKit.isLongArrayEmpty(null))
        assert(ArrayKit.isLongArrayEmpty(longArrayOf()))
        assertFalse(ArrayKit.isLongArrayEmpty(longArrayOf(1, 2)))
    }

    @Test
    fun isFloatArrayEmpty() {
        assert(ArrayKit.isFloatArrayEmpty(null))
        assert(ArrayKit.isFloatArrayEmpty(floatArrayOf()))
        assertFalse(ArrayKit.isFloatArrayEmpty(floatArrayOf(1.0F, 2.0F)))
    }

    @Test
    fun isDoubleArrayEmpty() {
        assert(ArrayKit.isDoubleArrayEmpty(null))
        assert(ArrayKit.isDoubleArrayEmpty(doubleArrayOf()))
        assertFalse(ArrayKit.isDoubleArrayEmpty(doubleArrayOf(1.0, 2.0)))
    }

    @Test
    fun isBooleanArrayEmpty() {
        assert(ArrayKit.isBooleanArrayEmpty(null))
        assert(ArrayKit.isBooleanArrayEmpty(booleanArrayOf()))
        assertFalse(ArrayKit.isBooleanArrayEmpty(booleanArrayOf(true, false)))
    }

    @Test
    fun isNotEmpty() {
        assertFalse(ArrayKit.isNotEmpty(null))
        assertFalse(ArrayKit.isNotEmpty(emptyArray<Int>()))
        assert(ArrayKit.isNotEmpty(arrayOf(1, 2)))
    }

    @Test
    fun isByteArrayNotEmpty() {
        assertFalse(ArrayKit.isByteArrayNotEmpty(null))
        assertFalse(ArrayKit.isByteArrayNotEmpty(byteArrayOf()))
        assert(ArrayKit.isByteArrayNotEmpty(byteArrayOf(1, 2)))
    }

    @Test
    fun isCharArrayNotEmpty() {
        assertFalse(ArrayKit.isCharArrayNotEmpty(null))
        assertFalse(ArrayKit.isCharArrayNotEmpty(charArrayOf()))
        assert(ArrayKit.isCharArrayNotEmpty(charArrayOf('a', 'b')))
    }

    @Test
    fun isShortArrayNotEmpty() {
        assertFalse(ArrayKit.isShortArrayNotEmpty(null))
        assertFalse(ArrayKit.isShortArrayNotEmpty(shortArrayOf()))
        assert(ArrayKit.isShortArrayNotEmpty(shortArrayOf(1, 2)))
    }

    @Test
    fun isIntArrayNotEmpty() {
        assertFalse(ArrayKit.isIntArrayNotEmpty(null))
        assertFalse(ArrayKit.isIntArrayNotEmpty(intArrayOf()))
        assert(ArrayKit.isIntArrayNotEmpty(intArrayOf(1, 2)))
    }

    @Test
    fun isLongArrayNotEmpty() {
        assertFalse(ArrayKit.isLongArrayNotEmpty(null))
        assertFalse(ArrayKit.isLongArrayNotEmpty(longArrayOf()))
        assert(ArrayKit.isLongArrayNotEmpty(longArrayOf(1, 2)))
    }

    @Test
    fun isFloatArrayNotEmpty() {
        assertFalse(ArrayKit.isFloatArrayNotEmpty(null))
        assertFalse(ArrayKit.isFloatArrayNotEmpty(floatArrayOf()))
        assert(ArrayKit.isFloatArrayNotEmpty(floatArrayOf(1.0F, 2.0F)))
    }

    @Test
    fun isDoubleArrayNotEmpty() {
        assertFalse(ArrayKit.isDoubleArrayNotEmpty(null))
        assertFalse(ArrayKit.isDoubleArrayNotEmpty(doubleArrayOf()))
        assert(ArrayKit.isDoubleArrayNotEmpty(doubleArrayOf(1.0, 2.0)))
    }

    @Test
    fun isBooleanArrayNotEmpty() {
        assertFalse(ArrayKit.isBooleanArrayNotEmpty(null))
        assertFalse(ArrayKit.isBooleanArrayNotEmpty(booleanArrayOf()))
        assert(ArrayKit.isBooleanArrayNotEmpty(booleanArrayOf(true, false)))
    }

}