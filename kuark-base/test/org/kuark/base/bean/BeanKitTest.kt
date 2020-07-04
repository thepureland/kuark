package org.kuark.base.bean

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

/**
 * BeanKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class BeanKitTest {

    private lateinit var person: Person

    @BeforeEach
    fun setUp() {
        val address = Address()
        with(address) {
            province = "hunan"
            city = "changsha"
            street = "wuyilu"
            zipcode = "410000"
        }
        val goods = mutableListOf("sporting", "singing", "dancing")
        val contact = mapOf("student" to "Tom", "teacher" to "Lucy")
        person = Person()
        with(person) {
            selfUniqueIdentifier = "id"
            name = "Mike"
            sex = "male"
            age = 25
            birthday = Date(60528873600000L)
            this.address = address
            this.goods = goods
            this.contact = contact
        }
    }

    @Test
    fun testCloneBean() {
        val p: Person = BeanKit.shallowClone(person)
        assertEquals(person, p)
        assertTrue(person.address === p.address) // 证明是浅克隆
    }

    @Test
    fun testDeepClone() {
        val p: Person = BeanKit.deepClone(person)
        assertEquals(person, p)
        assertTrue(person.address !== p.address) // 证明是深克隆
    }

    @Test
    fun testCopyProperties() {
        val p = Person()
        BeanKit.copyProperties(person, p)
        assertEquals(person, p)
        assertTrue(person.address === p.address) // 证明是浅克隆
    }

    @Test
    fun testCopyProperty() {
        BeanKit.copyProperty(person, "address.zipcode", "361000")
        assertEquals("361000", person.address?.zipcode)
    }

    @Test
    fun testCopyPropertiesWithoutCast() {
        BeanKit.copyProperty(person, "address.zipcode", "361000")
        assertEquals("361000", person.address?.zipcode)
    }

    @Test
    fun testExtract() {
        val map: Map<String, Any> = BeanKit.extract(person)
        assertEquals(person.age, map["age"])
        assertEquals(person.name, map["name"])
        assertEquals(person.sex, map["sex"])
        assertTrue(person.address === map["address"])
        assertTrue(person.birthday === map["birthday"])
        assertTrue(person.contact === map["contact"])
        assertTrue(person.goods === map["goods"])
    }

    @Test
    fun testGetProperty() {
        assertEquals(person.age, BeanKit.getProperty(person, "age"))
        assertEquals(person.goods?.get(0), BeanKit.getProperty(person, "goods[0]"))
        assertEquals(person.contact?.get("student"), BeanKit.getProperty(person, "contact(student)"))
        assertEquals(person.address?.province, BeanKit.getProperty(person, "address.province"))
    }

    @Test
    fun testCopyPropertiesByMap() {
        val list = listOf("1", "2")
        val srcObj = Pair("key", list)
        val propertyMap = mapOf("first" to "second","second[0]" to "first")

        val dest = BeanKit.copyProperties(Pair::class, srcObj, propertyMap)
        assertEquals(srcObj.first, dest?.second)
        assertEquals(srcObj.second[0], dest?.first)
    }

    @Test
    fun testCopyPropertiesToClassInstance() {
        val p: Person = BeanKit.copyProperties(person, Person::class)
        assertEquals(person, p)
        assertTrue(person.address === p.address) // 证明是浅克隆
    }

    @Test
    fun testCopyPropertiesExcludeId() {
        val dest = Person()
        BeanKit.copyPropertiesExcludeId(person, dest)
        assertEquals(person.age, dest.age)
        assertTrue(person.address === dest.address)
        assertNull(dest.selfUniqueIdentifier)
    }

    @Test
    fun testcopyPropertiesExclude() {
        val p = Person()
        BeanKit.copyPropertiesExclude(person, p, "age", "address")
        assertEquals(person.selfUniqueIdentifier, p.selfUniqueIdentifier)
        assertEquals(0, p.age)
        assertNull(p.address)
        assertTrue(person.goods === p.goods) // 浅克隆
    }

    @Test
    fun testResetPropertiesExcludeId() {
        val p: Person = BeanKit.shallowClone(person)
        BeanKit.resetPropertiesExcludeId(p)
        assertNull(p.name)
        assertNull(p.address)
        assertEquals(person.selfUniqueIdentifier, p.selfUniqueIdentifier)
    }

    @Test
    fun testBatchCopyProperties() {
        val persons = listOf<Person>(person, person)
        val results = BeanKit.batchCopyProperties(Person::class, persons)
        assertEquals(persons.size.toLong(), results.size.toLong())
        assertEquals(persons[0].name, results[0].name)
        assertEquals(persons[1].weight, results[1].weight, 0.0)
    }
}