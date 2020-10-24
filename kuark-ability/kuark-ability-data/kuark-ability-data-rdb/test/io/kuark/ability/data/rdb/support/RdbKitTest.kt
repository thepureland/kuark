package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.ability.data.rdb.metadata.RdbType
import io.kuark.test.SpringTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * RdbKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class RdbKitTest : SpringTest() {

    private val url = "jdbc:h2:tcp://localhost:9092/./h2;DATABASE_TO_LOWER=TRUE"

    @Test
    fun getDataSource() {
        RdbKit.getDataSource()
    }

    @Test
    fun getDatabase() {
        RdbKit.getDatabase()
    }

    @Test
    fun testConnection() {
        val connection = RdbKit.newConnection(url, "sa", null)
        assert(RdbKit.testConnection(connection))
    }

    @Test
    fun determinRdbTypeByUrl() {
        assertEquals(RdbType.H2, RdbKit.determinRdbTypeByUrl(url))
    }

    @Test
    fun getTestStatement() {
        assertEquals("select 1", RdbKit.getTestStatement(RdbType.H2))
    }

}