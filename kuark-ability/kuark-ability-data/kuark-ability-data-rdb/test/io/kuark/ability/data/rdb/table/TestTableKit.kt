package io.kuark.ability.data.rdb.table

import io.kuark.base.scanner.classpath.ClassPathScanner
import io.kuark.base.scanner.support.Resource
import io.kuark.context.kit.SpringKit
import org.springframework.jdbc.core.JdbcTemplate

/**
 * 测试表工具类
 *
 * @author K
 * @since 1.0.0
 */
internal object TestTableKit {

    const val TABLE_NAME = "test_table"

    private const val SCRIPT_CLASSPATH = "sql"
    private const val SCRIPT_SUFFIX = ".sql"

    fun create() {
        operateTestTable("create")
    }

    fun insert(): IntArray? {
        operateTestTable("insert")
        val ids = IntArray(11)
        for (i in 0..10) {
            ids[i] = i
        }
        return ids
    }

    fun drop() {
        operateTestTable("drop")
    }

    private fun operateTestTable(operate: String) {
        val resources: Array<Resource> = ClassPathScanner.scanForResources(SCRIPT_CLASSPATH, operate, SCRIPT_SUFFIX)
        val createTableSql: String = resources[0].loadAsString("UTF-8")!!
        SpringKit.getBean(JdbcTemplate::class).execute(createTableSql)
    }

}