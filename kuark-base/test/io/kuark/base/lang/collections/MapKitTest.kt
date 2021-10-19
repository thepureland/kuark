package io.kuark.base.lang.collections

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MapKitTest {

    @Test
    fun isEmpty() {
        assert(MapKit.isEmpty(null))
        assert(MapKit.isEmpty(emptyMap<String, Int>()))
        assertFalse(MapKit.isEmpty(mapOf(1 to "one")))
    }

    @Test
    fun isNotEmpty() {
        assertFalse(MapKit.isNotEmpty(null))
        assertFalse(MapKit.isNotEmpty(emptyMap<String, Int>()))
        assert(MapKit.isNotEmpty(mapOf(1 to "one")))
    }

}