package io.kuark.base.support

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class GroupExecutorTest {

    @Test
    fun testExecute() {
        val elems = IntRange(1, 50).toList()
        val sb = StringBuilder()
        object : GroupExecutor<Int>(elems, 10) {
            override fun groupExecute(subList: List<Int>) {
                if (subList.isNotEmpty()) {
                    sb.append(subList[0]).append(",")
                }
            }
        }.execute()
        assertEquals("1,11,21,31,41,", sb.toString())
    }

}