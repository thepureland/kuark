package io.kuark.base.lang.string

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

internal class StringKitTest {

    @Test
    fun isBlank() {
        assert(StringKit.isBlank(null))
        assert(StringKit.isBlank(""))
        assert(StringKit.isBlank(" "))
        assertFalse(StringKit.isBlank("null"))
    }

    @Test
    fun isEmpty(){
        assert(StringKit.isEmpty(null))
        assert(StringKit.isEmpty(""))
        assertFalse(StringKit.isEmpty(" "))
        assertFalse(StringKit.isEmpty("null"))
    }

    @Test
    fun isNotBlank() {
        assertFalse(StringKit.isNotBlank(null))
        assertFalse(StringKit.isNotBlank(""))
        assertFalse(StringKit.isNotBlank(" "))
        assert(StringKit.isNotBlank("null"))
    }

    @Test
    fun isNotEmpty(){
        assertFalse(StringKit.isNotEmpty(null))
        assertFalse(StringKit.isNotEmpty(""))
        assert(StringKit.isNotEmpty(" "))
        assert(StringKit.isNotEmpty("null"))
    }
    
}