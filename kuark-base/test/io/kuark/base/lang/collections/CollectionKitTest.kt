package io.kuark.base.lang.collections

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CollectionKitTest {

    @Test
    fun isEmpty() {
        assert(CollectionKit.isEmpty(null))
        assert(CollectionKit.isEmpty(emptyList<String>()))
        assertFalse(CollectionKit.isEmpty(setOf(1, 2)))
    }

    @Test
    fun isNotEmpty() {
        assertFalse(CollectionKit.isNotEmpty(null))
        assertFalse(CollectionKit.isNotEmpty(emptyList<String>()))
        assert(CollectionKit.isNotEmpty(setOf(1, 2)))
    }
}