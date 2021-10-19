package io.kuark.base.data.json

import com.fasterxml.jackson.core.type.TypeReference
import io.kuark.base.bean.Address
import io.kuark.base.bean.Person
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

/**
 * JsonKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class JsonKitTest {

    private lateinit var person: Person

    @BeforeEach
    fun setUp() {
        person = Person().apply {
            id = "id"
            name = "Mike"
            sex = "male"
            age = 25
            birthday = Date(60528873600000L)
            address = Address().apply {
                province = "hunan"
                city = "changsha"
                street = "wuyilu"
                zipcode = "410000"
            }
            goods = listOf("sporting", "singing", "dancing")
            contact = mapOf("student" to "Tom", "teacher" to "Lucy")
        }
    }

    @Test
    fun getPropertyValue() {
        val jsonStr = """
            {
                "active": true ,
                "status" :  1,
                "des":  "description " ,
                "message": "oh, err msg"
            }
        """
        assertEquals(true, JsonKit.getPropertyValue(jsonStr, "active"))
        assertEquals(1, JsonKit.getPropertyValue(jsonStr, "status"))
        assertEquals("description ", JsonKit.getPropertyValue(jsonStr, "des"))
        assertEquals("oh, err msg", JsonKit.getPropertyValue(jsonStr, "message"))
        assertEquals(null, JsonKit.getPropertyValue(jsonStr, "no_exist"))
    }

    @Test
    fun jsonToDisplay() {
        assertEquals("A:b,B:b", JsonKit.jsonToDisplay("""{"A":"b","B":'b'}"""))
        assertEquals("", JsonKit.jsonToDisplay(" "))
    }

    @Test
    fun fromJson() {
        assertEquals(null, JsonKit.fromJson("", Person::class))
        assertEquals(null, JsonKit.fromJson("sdfsdf", Person::class))

        val json = "[\"abc\",\"cde\"]"
        val strings = JsonKit.fromJson(json, Array<String>::class)!!
        assertEquals(strings[0], "abc")
        assertEquals(strings[1], "cde")
    }

    @Test
    fun testFromJson() {
        val jsonStr =
            """[{"selfUniqueIdentifier":null,"parentUniqueIdentifier":null,"name":"Mike","sex":"male","age":25,"weight":0.0,"birthday":60528873600000,"address":{"province":"hunan","city":"changsha","street":"wuyilu","zipcode":"410000"},"goods":["sporting","singing","dancing"],"contact":{"student":"Tom","teacher":"Lucy"},"id":null,"pId":null}]"""
        val persons = JsonKit.fromJson(jsonStr, object : TypeReference<List<Person>>() {})
        assertEquals(1, persons!!.size)
    }

    @Test
    fun testToJson() {
        val jsonStr =
            """[{"selfUniqueIdentifier":null,"parentUniqueIdentifier":null,"name":"Mike","sex":"male","age":25,"weight":0.0,"birthday":60528873600000,"address":{"province":"hunan","city":"changsha","street":"wuyilu","zipcode":"410000"},"goods":["sporting","singing","dancing"],"contact":{"student":"Tom","teacher":"Lucy"},"id":null,"pId":null}]"""
        assertEquals(jsonStr.length, JsonKit.toJson(listOf(person)).length)
    }

    @Test
    fun updateBean() {
        val jsonStr =
            """{"selfUniqueIdentifier":null,"parentUniqueIdentifier":null,"name":"Mike","sex":"male","age":25,"weight":0.0,"birthday":60528873600000,"address":{"province":"hunan","city":"changsha","street":"wuyilu","zipcode":"410000"},"goods":["sporting","singing","dancing"],"contact":{"student":"Tom","teacher":"Lucy"},"id":null,"pId":null}"""
        val person = Person().apply {
            name = "unknown"
            address = Address().apply {
                province = "unknown"
            }
        }
        val result = JsonKit.updateBean(jsonStr, person)!!
        assertEquals("Mike", result.name)
        assertEquals("hunan", result.address!!.province)
    }

    @Test
    fun toJsonP() {
        val jsonP =
            """func({"selfUniqueIdentifier":null,"parentUniqueIdentifier":null,"name":"Mike","sex":"male","age":25,"weight":0.0,"birthday":60528873600000,"address":{"province":"hunan","city":"changsha","street":"wuyilu","zipcode":"410000"},"goods":["sporting","singing","dancing"],"contact":{"student":"Tom","teacher":"Lucy"},"id":null,"pId":null})"""
        assertEquals(jsonP.length, JsonKit.toJsonP("func", person).length)
    }

}