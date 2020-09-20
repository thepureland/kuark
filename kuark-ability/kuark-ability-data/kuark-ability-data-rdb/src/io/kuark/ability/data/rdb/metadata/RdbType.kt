package io.kuark.ability.data.rdb.metadata

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

        /**
         * 返回产品名称对应的关系型数据库类型枚举
         *
         * @param productName 产品名称
         * @return 关系型数据库类型枚举
         * @author K
         * @since 1.0.0
         */
        fun ofProductName(productName: String): RdbType = values().first { it.productName == productName }

        /**
         * 返回JDBC驱动名称对应的关系型数据库类型枚举
         *
         * @param jdbcDriverName JDBC驱动名称
         * @return 关系型数据库类型枚举
         * @author K
         * @since 1.0.0
         */
        fun ofJdbcDriverName(jdbcDriverName: String): RdbType = values().first { it.jdbcDriverName == jdbcDriverName }

    }

}