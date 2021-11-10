package io.kuark.distributed.tx.table

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

    fun insert() {
        operateTestTable("insert")
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