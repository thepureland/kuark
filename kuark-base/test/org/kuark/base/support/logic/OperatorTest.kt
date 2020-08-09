package org.kuark.base.support.logic

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * Operator测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class OperatorTest {

    @Test
    fun compare() {
        // EQ("=", "等于", )
        assert(Operator.EQ.assert(null, null))
        assert(Operator.EQ.assert("1", "1"))
        assert(Operator.EQ.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.EQ.assert(null, ""))
        assertFalse(Operator.EQ.assert("1", "2"))
        assertFalse(Operator.EQ.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // IEQ("I=", "忽略大小写等于", 值不可接受null, 操作值只接收字符串类型)
        assert(Operator.IEQ.assert(null, null))
        assert(Operator.IEQ.assert("A", "a"))
        assert(Operator.IEQ.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.IEQ.assert(null, ""))
        assertFalse(Operator.IEQ.assert("A", "b"))
        assertFalse(Operator.IEQ.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // NE("!=", "不等于", 值不可接受null)
        assertFalse(Operator.NE.assert(null, null))
        assertFalse(Operator.NE.assert("1", "1"))
        assertFalse(Operator.NE.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assert(Operator.NE.assert(null, ""))
        assert(Operator.NE.assert("1", "2"))
        assert(Operator.NE.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // LG("<>", "小于大于(不等于)")
        assertFalse(Operator.LG.assert(null, null))
        assertFalse(Operator.LG.assert("1", "1"))
        assertFalse(Operator.LG.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assert(Operator.LG.assert(null, ""))
        assert(Operator.LG.assert("1", "2"))
        assert(Operator.LG.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // GE(">=", "大于等于")
        assert(Operator.GE.assert(null, null))
        assert(Operator.GE.assert("1", "1"))
        assert(Operator.GE.assert(2, 1))
        assert(Operator.GE.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.GE.assert(null, ""))
        assertFalse(Operator.GE.assert(1, 2))
        assertFalse(Operator.GE.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // LE("<=", "小于等于")
        assert(Operator.LE.assert(null, null))
        assert(Operator.LE.assert("1", "1"))
        assert(Operator.LE.assert(1, 2))
        assert(Operator.LE.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.LE.assert(null, ""))
        assertFalse(Operator.LE.assert(2, 1))
        assertFalse(Operator.LE.assert(KeyValue("k1", "v"), KeyValue("k", "v1")))

        // GT(">", "大于")
        assert(Operator.GT.assert(2, 1))
        assert(Operator.GT.assert(KeyValue("k2", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.GT.assert(null, null))
        assertFalse(Operator.GT.assert("1", "1"))
        assertFalse(Operator.GT.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.GT.assert(null, ""))
        assertFalse(Operator.GT.assert(1, 2))
        assertFalse(Operator.GT.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // LT("<", "小于")
        assert(Operator.LT.assert(1, 2))
        assert(Operator.LT.assert(KeyValue("k1", "v1"), KeyValue("k2", "v1")))
        assertFalse(Operator.LT.assert(null, null))
        assertFalse(Operator.LT.assert("1", "1"))
        assertFalse(Operator.LT.assert(null, ""))
        assertFalse(Operator.LT.assert(2, 1))
        assertFalse(Operator.LT.assert(KeyValue("k1", "v"), KeyValue("k", "v1")))

        // LIKE("LIKE", "任意位置匹配", 值不可接受null, 操作值只接收字符串类型)
        assert(Operator.LIKE.assert("1", "1"))
        assert(Operator.LIKE.assert("212", "1"))
        assert(Operator.LIKE.assert("212", ""))
        assertFalse(Operator.LIKE.assert(null, null))
        assertFalse(Operator.LIKE.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.LIKE.assert(null, ""))
        assertFalse(Operator.LIKE.assert("1", "2"))
        assertFalse(Operator.LIKE.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // LIKE_S("LIKE_S", "匹配前面", 值不可接受null, 操作值只接收字符串类型)
        assert(Operator.LIKE_S.assert("1", "1"))
        assert(Operator.LIKE_S.assert("123", "1"))
        assert(Operator.LIKE_S.assert("1", ""))
        assertFalse(Operator.LIKE_S.assert("321", "1"))
        assertFalse(Operator.LIKE_S.assert("212", "1"))
        assertFalse(Operator.LIKE_S.assert(null, null))
        assertFalse(Operator.LIKE_S.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.LIKE_S.assert(null, ""))
        assertFalse(Operator.LIKE_S.assert("1", "2"))
        assertFalse(Operator.LIKE_S.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // LIKE_E("LIKE_E", "匹配后面", 值不可接受null, 操作值只接收字符串类型)
        assert(Operator.LIKE_E.assert("1", "1"))
        assert(Operator.LIKE_E.assert("321", "1"))
        assert(Operator.LIKE_E.assert("1", ""))
        assertFalse(Operator.LIKE_E.assert("123", "1"))
        assertFalse(Operator.LIKE_E.assert("212", "1"))
        assertFalse(Operator.LIKE_E.assert(null, null))
        assertFalse(Operator.LIKE_E.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.LIKE_E.assert(null, ""))
        assertFalse(Operator.LIKE_E.assert("1", "2"))
        assertFalse(Operator.LIKE_E.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // ILIKE("ILIKE", "忽略大小写任意位置匹配", 值不可接受null, 操作值只接收字符串类型)
        assert(Operator.ILIKE.assert("1", "1"))
        assert(Operator.ILIKE.assert("212", "1"))
        assert(Operator.ILIKE.assert("212", ""))
        assert(Operator.ILIKE.assert("ABC", "abc"))
        assert(Operator.ILIKE.assert("abc", "AB"))
        assertFalse(Operator.ILIKE.assert(null, null))
        assertFalse(Operator.ILIKE.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.ILIKE.assert(null, ""))
        assertFalse(Operator.ILIKE.assert("1", "2"))
        assertFalse(Operator.ILIKE.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // ILIKE_S("ILIKE_S", "忽略大小写匹配前面", 值不可接受null, 操作值只接收字符串类型)
        assert(Operator.ILIKE_S.assert("1", "1"))
        assert(Operator.ILIKE_S.assert("212", "2"))
        assert(Operator.ILIKE_S.assert("212", ""))
        assert(Operator.ILIKE_S.assert("ABC", "abc"))
        assert(Operator.ILIKE_S.assert("abc", "AB"))
        assertFalse(Operator.ILIKE_S.assert(null, null))
        assertFalse(Operator.ILIKE_S.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.ILIKE_S.assert(null, ""))
        assertFalse(Operator.ILIKE_S.assert("1", "2"))
        assertFalse(Operator.ILIKE_S.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // ILIKE_E("ILIKE_E", "忽略大小写匹配后面", 值不可接受null, 操作值只接收字符串类型)
        assert(Operator.ILIKE_E.assert("1", "1"))
        assert(Operator.ILIKE_E.assert("212", "2"))
        assert(Operator.ILIKE_E.assert("212", ""))
        assert(Operator.ILIKE_E.assert("ABC", "abc"))
        assert(Operator.ILIKE_E.assert("abc", "BC"))
        assertFalse(Operator.ILIKE_E.assert(null, null))
        assertFalse(Operator.ILIKE_E.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.ILIKE_E.assert(null, ""))
        assertFalse(Operator.ILIKE_E.assert("1", "2"))
        assertFalse(Operator.ILIKE_E.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // IN("IN", "in查询")
        assert(Operator.IN.assert("1", "1"))
        assert(Operator.IN.assert("1", "1,2,3"))
        assert(Operator.IN.assert(1, arrayOf(1,2,3)))
        assert(Operator.IN.assert(arrayOf(1), arrayOf(1,2,3)))
        assert(Operator.IN.assert(1, listOf(1,2,3)))
        assert(Operator.IN.assert(listOf(1), listOf(1,2,3)))
        assert(Operator.IN.assert(1, setOf(1,2,3)))
        assert(Operator.IN.assert(setOf(1), setOf(1,2,3)))
        assert(Operator.IN.assert(mapOf(1 to 1), mapOf(1 to 1, 2 to 2)))
        assertFalse(Operator.IN.assert("1", "212"))
        assertFalse(Operator.IN.assert("", "212"))
        assertFalse(Operator.IN.assert(null, null))
        assertFalse(Operator.IN.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(Operator.IN.assert(null, ""))
        assertFalse(Operator.IN.assert("1", "2"))
        assertFalse(Operator.IN.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // NOT_IN("NOT IN", "not in查询") in查询
        assertFalse(Operator.NOT_IN.assert("1", "1"))
        assertFalse(Operator.NOT_IN.assert("1", "1,2,3"))
        assertFalse(Operator.NOT_IN.assert(1, arrayOf(1,2,3)))
        assertFalse(Operator.NOT_IN.assert(arrayOf(1), arrayOf(1,2,3)))
        assertFalse(Operator.NOT_IN.assert(1, listOf(1,2,3)))
        assertFalse(Operator.NOT_IN.assert(listOf(1), listOf(1,2,3)))
        assertFalse(Operator.NOT_IN.assert(1, setOf(1,2,3)))
        assertFalse(Operator.NOT_IN.assert(setOf(1), setOf(1,2,3)))
        assertFalse(Operator.NOT_IN.assert(mapOf(1 to 1), mapOf(1 to 1, 2 to 2)))
        assert(Operator.NOT_IN.assert("1", "212"))
        assert(Operator.NOT_IN.assert("", "212"))
        assert(Operator.NOT_IN.assert(null, null))
        assert(Operator.NOT_IN.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assert(Operator.NOT_IN.assert(null, ""))
        assert(Operator.NOT_IN.assert("1", "2"))
        assert(Operator.NOT_IN.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // IS_NULL("IS NULL", "判空", 值可接受null)
        assert(Operator.IS_NULL.assert(null))
        assertFalse(Operator.IS_NULL.assert("null"))
        assertFalse(Operator.IS_NULL.assert(""))
        assertFalse(Operator.IS_NULL.assert(listOf<String>()))

        // IS_NOT_NULL("IS NOT NULL", "非空", 值可接受null)
        assertFalse(Operator.IS_NOT_NULL.assert(null))
        assert(Operator.IS_NOT_NULL.assert("null"))
        assert(Operator.IS_NOT_NULL.assert(""))
        assert(Operator.IS_NOT_NULL.assert(listOf<String>()))

        // IS_EMPTY("=''", "等于空串", 值可接受null, 操作值只接收字符串类型)
        assert(Operator.IS_EMPTY.assert(""))
        assert(Operator.IS_EMPTY.assert(arrayOf<String>()))
        assert(Operator.IS_EMPTY.assert(listOf<String>()))
        assert(Operator.IS_EMPTY.assert(setOf<String>()))
        assert(Operator.IS_EMPTY.assert(mapOf<String, String>()))
        assertFalse(Operator.IS_EMPTY.assert(null))
        assertFalse(Operator.IS_EMPTY.assert("null"))
        assertFalse(Operator.IS_EMPTY.assert(" "))

        // IS_NOT_EMPTY("!=''", "不等于空串", 值可接受null, 操作值只接收字符串类型)
        assertFalse(Operator.IS_NOT_EMPTY.assert(""))
        assertFalse(Operator.IS_NOT_EMPTY.assert(arrayOf<String>()))
        assertFalse(Operator.IS_NOT_EMPTY.assert(listOf<String>()))
        assertFalse(Operator.IS_NOT_EMPTY.assert(setOf<String>()))
        assertFalse(Operator.IS_NOT_EMPTY.assert(mapOf<String, String>()))
        assert(Operator.IS_NOT_EMPTY.assert(null))
        assert(Operator.IS_NOT_EMPTY.assert("null"))
        assert(Operator.IS_NOT_EMPTY.assert(" "))
    }
    
    internal data class KeyValue(
        val k: String,
        val v: String
    ): Comparable<KeyValue> {
        override fun compareTo(other: KeyValue): Int {
            return k.compareTo(other.k)
        }
    }
}