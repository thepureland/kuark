package org.kuark.data.jdbc.metadata

/**
 * 支持的关系型数据库类型枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class RdbType(val productName: String, val jdbcDriverName: String) {

    H2("H2", "org.h2.Driver"),
    MYSQL("MySQL", "com.mysql.jdbc.Driver"),
    POSTGRESQL("PostgreSQL", "org.postgresql.Driver"),
    ORACLE("Oracle", "oracle.jdbc.driver.OracleDriver"),
    SQLITE("SQLite", "org.sqlite.JDBC"),
    DB2("DB2", "com.ibm.db2.jcc.DB2Driver"),
    SQLSERVER("Sql Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

    companion object {

        fun productNameOf(productName: String): RdbType = values().first { it.productName == productName }

        fun jdbcDriverNameOf(jdbcDriverName: String): RdbType = values().first { it.jdbcDriverName == jdbcDriverName }

    }

}