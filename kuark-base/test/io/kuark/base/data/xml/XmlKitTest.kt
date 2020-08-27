package io.kuark.base.data.xml

import io.kuark.base.bean.Address
import io.kuark.base.lang.string.countMatches
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.xml.bind.annotation.*
import javax.xml.bind.annotation.adapters.XmlAdapter
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

/**
 * XmlKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class XmlKitTest {

    private lateinit var person: Person

    @BeforeEach
    fun setUp() {
        val address = Address().apply {
            province = "hunan"
            city = "changsha"
            street = "wuyilu"
            zipcode = "410000"
        }
        val goods = listOf("sporting", "singing", "dancing")
        val contact = mapOf("student" to "Tom", "teacher" to "Lucy")
        val birthday = LocalDate.of(2020, 8, 27)
        person = Person("Mike", "male", null, birthday, address, goods, contact)
    }

    @Test
    fun toXml() {
        var xml = XmlKit.toXml(person)
        println(xml)
        assert(xml.contains("<zipcode>410000</zipcode>"))

        val student = Student().apply {
            name = "Mike"
            sex = "male"
            weight = null
            birthday = LocalDate.of(2020, 8, 27)
            goods = listOf("sporting", "singing", "dancing")
            contact = mapOf("student" to "Tom", "teacher" to "Lucy")
        }
        xml = XmlKit.toXml(student)
        println(xml)
        assert(xml.contains("<goodses>"))
        assert(!xml.contains("<contact>"))
    }

    @Test
    fun testToXml() {
        val xml = XmlKit.toXml(listOf(person, person), "persons", Person::class)
        print(xml)
        assertEquals(2, xml.countMatches("<person>"))
    }

    @Test
    fun fromXml() {
        val xml = XmlKit.toXml(person)
        val p = XmlKit.fromXml(xml, Person::class)
        assertEquals(person.name, p.name)
        assertEquals(person.address, p.address)
        assertEquals(person.contact, p.contact)
    }

    @XmlRootElement // 必须要有
    internal data class Person(
        var name: String?,
        var sex: String?,
        @set:XmlElement(nillable = true, namespace = "") // 会被映射为xml元素
        var weight: Double?,
        @set:XmlJavaTypeAdapter(DateAdapter::class)
        var birthday: LocalDate?,
        var address: Address?,
        var goods: List<String>?,
        var contact: Map<String, String>?
    ) {
        constructor() : this(null, null, null, null, null, null, null)
    }

    @XmlRootElement(name = "student")
    @XmlAccessorType(XmlAccessType.PUBLIC_MEMBER) // 仅public的属性会被映射为xml元素
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL) // 按字母顺序
    internal class Student {
        var name: String? = null
        var sex: String? = null

        @set:XmlAttribute(required = true) // 同样未被映射为xml元素
        var weight: Double? = null
        var birthday: LocalDate? = null
        var address: Address? = null

        @set:XmlElementWrapper(name = "goodses") // 同名的元素被包装为goodses的子元素
        var goods: List<String>? = null

        @set:XmlTransient  // 不映射为xml元素
        var contact: Map<String, String>? = null
    }

    private class DateAdapter : XmlAdapter<String, LocalDate>() {

        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        override fun unmarshal(date: String): LocalDate {
            return formatter.parse(date) as LocalDate
        }

        override fun marshal(date: LocalDate): String {
            return date.format(formatter)
        }

    }

}