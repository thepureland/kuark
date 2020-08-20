package io.kuark.base.data.json

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import io.kuark.base.bean.Address
import io.kuark.base.bean.Person
import io.kuark.base.tree.TreeKit
import java.util.*

internal class JsonKitTest {

    private lateinit var person: Person

    @BeforeEach
    fun setUp() {
        person = Person()
        with(person) {
            id = "id"
            name = "Mike"
            sex = "male"
            age = 25
            birthday = Date(60528873600000L)
            this.address = Address("hunan", "changsha", "wuyilu", "410000")
            this.goods = listOf("sporting", "singing", "dancing")
            this.contact = mapOf("student" to "Tom", "teacher" to "Lucy")
        }

    }

    @Test
    fun jsonToDisplay() {
    }

    @Test
    fun fromJson() {
        val json = "[\"abc\",\"cde\"]"
        val strings = JsonKit.fromJson(json, Array<String>::class)!!
        assertEquals(strings[0], "abc")
        assertEquals(strings[1], "cde")
    }

    @Test
    fun testFromJson() {
    }

    @Test
    fun testFromJson1() {
    }

    @Test
    fun testFromJson2() {
    }

    @Test
    fun testFromJson3() {
    }

    @Test
    fun testFromJson4() {
    }

    @Test
    fun createCollectionType() {
    }

    @Test
    fun testCreateCollectionType() {
    }

    @Test
    fun testFromJson5() {
    }

    @Test
    fun testFromJson6() {
    }

    @Test
    fun testFromJson7() {
    }

    @Test
    fun testFromJson8() {
    }

    @Test
    fun toJson() {
    }

    @Test
    fun testToJson() {
        val persons = listOf(person)
        val treeNodes = TreeKit.convertListToTree(persons)
        val s = JsonKit.toJson(treeNodes)
        println(s)
    }

    @Test
    fun createMapper() {
    }

    @Test
    fun createNonEmptyMapper() {
    }

    @Test
    fun createNonDefaultMapper() {
    }

    @Test
    fun updateBean() {
    }

    @Test
    fun testUpdateBean() {
    }

    @Test
    fun toJsonP() {
    }

    @Test
    fun enableEnumUseToString() {
    }

    @Test
    fun enableJaxbAnnotation() {
    }

    @Test
    fun enableSimple() {
    }
}