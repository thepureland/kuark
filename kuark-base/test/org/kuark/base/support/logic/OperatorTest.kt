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
        assert(LogicOperator.EQ.assert(null, null))
        assert(LogicOperator.EQ.assert("1", "1"))
        assert(LogicOperator.EQ.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.EQ.assert(null, ""))
        assertFalse(LogicOperator.EQ.assert("1", "2"))
        assertFalse(LogicOperator.EQ.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // IEQ("I=", "忽略大小写等于", 值不可接受null, 操作值只接收字符串类型)
        assert(LogicOperator.IEQ.assert(null, null))
        assert(LogicOperator.IEQ.assert("A", "a"))
        assert(LogicOperator.IEQ.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.IEQ.assert(null, ""))
        assertFalse(LogicOperator.IEQ.assert("A", "b"))
        assertFalse(LogicOperator.IEQ.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // NE("!=", "不等于", 值不可接受null)
        assertFalse(LogicOperator.NE.assert(null, null))
        assertFalse(LogicOperator.NE.assert("1", "1"))
        assertFalse(LogicOperator.NE.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assert(LogicOperator.NE.assert(null, ""))
        assert(LogicOperator.NE.assert("1", "2"))
        assert(LogicOperator.NE.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // LG("<>", "小于大于(不等于)")
        assertFalse(LogicOperator.LG.assert(null, null))
        assertFalse(LogicOperator.LG.assert("1", "1"))
        assertFalse(LogicOperator.LG.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assert(LogicOperator.LG.assert(null, ""))
        assert(LogicOperator.LG.assert("1", "2"))
        assert(LogicOperator.LG.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // GE(">=", "大于等于")
        assert(LogicOperator.GE.assert(null, null))
        assert(LogicOperator.GE.assert("1", "1"))
        assert(LogicOperator.GE.assert(2, 1))
        assert(LogicOperator.GE.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.GE.assert(null, ""))
        assertFalse(LogicOperator.GE.assert(1, 2))
        assertFalse(LogicOperator.GE.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // LE("<=", "小于等于")
        assert(LogicOperator.LE.assert(null, null))
        assert(LogicOperator.LE.assert("1", "1"))
        assert(LogicOperator.LE.assert(1, 2))
        assert(LogicOperator.LE.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.LE.assert(null, ""))
        assertFalse(LogicOperator.LE.assert(2, 1))
        assertFalse(LogicOperator.LE.assert(KeyValue("k1", "v"), KeyValue("k", "v1")))

        // GT(">", "大于")
        assert(LogicOperator.GT.assert(2, 1))
        assert(LogicOperator.GT.assert(KeyValue("k2", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.GT.assert(null, null))
        assertFalse(LogicOperator.GT.assert("1", "1"))
        assertFalse(LogicOperator.GT.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.GT.assert(null, ""))
        assertFalse(LogicOperator.GT.assert(1, 2))
        assertFalse(LogicOperator.GT.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // LT("<", "小于")
        assert(LogicOperator.LT.assert(1, 2))
        assert(LogicOperator.LT.assert(KeyValue("k1", "v1"), KeyValue("k2", "v1")))
        assertFalse(LogicOperator.LT.assert(null, null))
        assertFalse(LogicOperator.LT.assert("1", "1"))
        assertFalse(LogicOperator.LT.assert(null, ""))
        assertFalse(LogicOperator.LT.assert(2, 1))
        assertFalse(LogicOperator.LT.assert(KeyValue("k1", "v"), KeyValue("k", "v1")))

        // LIKE("LIKE", "任意位置匹配", 值不可接受null, 操作值只接收字符串类型)
        assert(LogicOperator.LIKE.assert("1", "1"))
        assert(LogicOperator.LIKE.assert("212", "1"))
        assert(LogicOperator.LIKE.assert("212", ""))
        assertFalse(LogicOperator.LIKE.assert(null, null))
        assertFalse(LogicOperator.LIKE.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.LIKE.assert(null, ""))
        assertFalse(LogicOperator.LIKE.assert("1", "2"))
        assertFalse(LogicOperator.LIKE.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // LIKE_S("LIKE_S", "匹配前面", 值不可接受null, 操作值只接收字符串类型)
        assert(LogicOperator.LIKE_S.assert("1", "1"))
        assert(LogicOperator.LIKE_S.assert("123", "1"))
        assert(LogicOperator.LIKE_S.assert("1", ""))
        assertFalse(LogicOperator.LIKE_S.assert("321", "1"))
        assertFalse(LogicOperator.LIKE_S.assert("212", "1"))
        assertFalse(LogicOperator.LIKE_S.assert(null, null))
        assertFalse(LogicOperator.LIKE_S.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.LIKE_S.assert(null, ""))
        assertFalse(LogicOperator.LIKE_S.assert("1", "2"))
        assertFalse(LogicOperator.LIKE_S.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // LIKE_E("LIKE_E", "匹配后面", 值不可接受null, 操作值只接收字符串类型)
        assert(LogicOperator.LIKE_E.assert("1", "1"))
        assert(LogicOperator.LIKE_E.assert("321", "1"))
        assert(LogicOperator.LIKE_E.assert("1", ""))
        assertFalse(LogicOperator.LIKE_E.assert("123", "1"))
        assertFalse(LogicOperator.LIKE_E.assert("212", "1"))
        assertFalse(LogicOperator.LIKE_E.assert(null, null))
        assertFalse(LogicOperator.LIKE_E.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.LIKE_E.assert(null, ""))
        assertFalse(LogicOperator.LIKE_E.assert("1", "2"))
        assertFalse(LogicOperator.LIKE_E.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // ILIKE("ILIKE", "忽略大小写任意位置匹配", 值不可接受null, 操作值只接收字符串类型)
        assert(LogicOperator.ILIKE.assert("1", "1"))
        assert(LogicOperator.ILIKE.assert("212", "1"))
        assert(LogicOperator.ILIKE.assert("212", ""))
        assert(LogicOperator.ILIKE.assert("ABC", "abc"))
        assert(LogicOperator.ILIKE.assert("abc", "AB"))
        assertFalse(LogicOperator.ILIKE.assert(null, null))
        assertFalse(LogicOperator.ILIKE.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.ILIKE.assert(null, ""))
        assertFalse(LogicOperator.ILIKE.assert("1", "2"))
        assertFalse(LogicOperator.ILIKE.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // ILIKE_S("ILIKE_S", "忽略大小写匹配前面", 值不可接受null, 操作值只接收字符串类型)
        assert(LogicOperator.ILIKE_S.assert("1", "1"))
        assert(LogicOperator.ILIKE_S.assert("212", "2"))
        assert(LogicOperator.ILIKE_S.assert("212", ""))
        assert(LogicOperator.ILIKE_S.assert("ABC", "abc"))
        assert(LogicOperator.ILIKE_S.assert("abc", "AB"))
        assertFalse(LogicOperator.ILIKE_S.assert(null, null))
        assertFalse(LogicOperator.ILIKE_S.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.ILIKE_S.assert(null, ""))
        assertFalse(LogicOperator.ILIKE_S.assert("1", "2"))
        assertFalse(LogicOperator.ILIKE_S.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // ILIKE_E("ILIKE_E", "忽略大小写匹配后面", 值不可接受null, 操作值只接收字符串类型)
        assert(LogicOperator.ILIKE_E.assert("1", "1"))
        assert(LogicOperator.ILIKE_E.assert("212", "2"))
        assert(LogicOperator.ILIKE_E.assert("212", ""))
        assert(LogicOperator.ILIKE_E.assert("ABC", "abc"))
        assert(LogicOperator.ILIKE_E.assert("abc", "BC"))
        assertFalse(LogicOperator.ILIKE_E.assert(null, null))
        assertFalse(LogicOperator.ILIKE_E.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.ILIKE_E.assert(null, ""))
        assertFalse(LogicOperator.ILIKE_E.assert("1", "2"))
        assertFalse(LogicOperator.ILIKE_E.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // IN("IN", "in查询")
        assert(LogicOperator.IN.assert("1", "1"))
        assert(LogicOperator.IN.assert("1", "1,2,3"))
        assert(LogicOperator.IN.assert(1, arrayOf(1,2,3)))
        assert(LogicOperator.IN.assert(arrayOf(1), arrayOf(1,2,3)))
        assert(LogicOperator.IN.assert(1, listOf(1,2,3)))
        assert(LogicOperator.IN.assert(listOf(1), listOf(1,2,3)))
        assert(LogicOperator.IN.assert(1, setOf(1,2,3)))
        assert(LogicOperator.IN.assert(setOf(1), setOf(1,2,3)))
        assert(LogicOperator.IN.assert(mapOf(1 to 1), mapOf(1 to 1, 2 to 2)))
        assertFalse(LogicOperator.IN.assert("1", "212"))
        assertFalse(LogicOperator.IN.assert("", "212"))
        assertFalse(LogicOperator.IN.assert(null, null))
        assertFalse(LogicOperator.IN.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assertFalse(LogicOperator.IN.assert(null, ""))
        assertFalse(LogicOperator.IN.assert("1", "2"))
        assertFalse(LogicOperator.IN.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // NOT_IN("NOT IN", "not in查询") in查询
        assertFalse(LogicOperator.NOT_IN.assert("1", "1"))
        assertFalse(LogicOperator.NOT_IN.assert("1", "1,2,3"))
        assertFalse(LogicOperator.NOT_IN.assert(1, arrayOf(1,2,3)))
        assertFalse(LogicOperator.NOT_IN.assert(arrayOf(1), arrayOf(1,2,3)))
        assertFalse(LogicOperator.NOT_IN.assert(1, listOf(1,2,3)))
        assertFalse(LogicOperator.NOT_IN.assert(listOf(1), listOf(1,2,3)))
        assertFalse(LogicOperator.NOT_IN.assert(1, setOf(1,2,3)))
        assertFalse(LogicOperator.NOT_IN.assert(setOf(1), setOf(1,2,3)))
        assertFalse(LogicOperator.NOT_IN.assert(mapOf(1 to 1), mapOf(1 to 1, 2 to 2)))
        assert(LogicOperator.NOT_IN.assert("1", "212"))
        assert(LogicOperator.NOT_IN.assert("", "212"))
        assert(LogicOperator.NOT_IN.assert(null, null))
        assert(LogicOperator.NOT_IN.assert(KeyValue("k1", "v1"), KeyValue("k1", "v1")))
        assert(LogicOperator.NOT_IN.assert(null, ""))
        assert(LogicOperator.NOT_IN.assert("1", "2"))
        assert(LogicOperator.NOT_IN.assert(KeyValue("k", "v"), KeyValue("k1", "v1")))

        // IS_NULL("IS NULL", "判空", 值可接受null)
        assert(LogicOperator.IS_NULL.assert(null))
        assertFalse(LogicOperator.IS_NULL.assert("null"))
        assertFalse(LogicOperator.IS_NULL.assert(""))
        assertFalse(LogicOperator.IS_NULL.assert(listOf<String>()))

        // IS_NOT_NULL("IS NOT NULL", "非空", 值可接受null)
        assertFalse(LogicOperator.IS_NOT_NULL.assert(null))
        assert(LogicOperator.IS_NOT_NULL.assert("null"))
        assert(LogicOperator.IS_NOT_NULL.assert(""))
        assert(LogicOperator.IS_NOT_NULL.assert(listOf<String>()))

        // IS_EMPTY("=''", "等于空串", 值可接受null, 操作值只接收字符串类型)
        assert(LogicOperator.IS_EMPTY.assert(""))
        assert(LogicOperator.IS_EMPTY.assert(arrayOf<String>()))
        assert(LogicOperator.IS_EMPTY.assert(listOf<String>()))
        assert(LogicOperator.IS_EMPTY.assert(setOf<String>()))
        assert(LogicOperator.IS_EMPTY.assert(mapOf<String, String>()))
        assertFalse(LogicOperator.IS_EMPTY.assert(null))
        assertFalse(LogicOperator.IS_EMPTY.assert("null"))
        assertFalse(LogicOperator.IS_EMPTY.assert(" "))

        // IS_NOT_EMPTY("!=''", "不等于空串", 值可接受null, 操作值只接收字符串类型)
        assertFalse(LogicOperator.IS_NOT_EMPTY.assert(""))
        assertFalse(LogicOperator.IS_NOT_EMPTY.assert(arrayOf<String>()))
        assertFalse(LogicOperator.IS_NOT_EMPTY.assert(listOf<String>()))
        assertFalse(LogicOperator.IS_NOT_EMPTY.assert(setOf<String>()))
        assertFalse(LogicOperator.IS_NOT_EMPTY.assert(mapOf<String, String>()))
        assert(LogicOperator.IS_NOT_EMPTY.assert(null))
        assert(LogicOperator.IS_NOT_EMPTY.assert("null"))
        assert(LogicOperator.IS_NOT_EMPTY.assert(" "))
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