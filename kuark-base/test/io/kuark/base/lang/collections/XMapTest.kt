package io.kuark.base.lang.collections

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * XMap测试用例
 *
 * @author K
 * @since 1.0.0
 */
class XMapTest {

    @Test
    fun testToArrOfArr() {
        val map = mapOf("k1" to "v1", "k2" to "v2", "k3" to "v3")
        val arrOfArr = map.toArrOfArr()
        Assertions.assertEquals(3, arrOfArr.size.toLong())
        Assertions.assertEquals("k1", arrOfArr[0][0])
        Assertions.assertEquals("v1", arrOfArr[0][1])
        Assertions.assertEquals("k2", arrOfArr[1][0])
        Assertions.assertEquals("v2", arrOfArr[1][1])
        Assertions.assertEquals("k3", arrOfArr[2][0])
        Assertions.assertEquals("v3", arrOfArr[2][1])
    }

}