package org.kuark.data.jdbc.datasource

import com.zaxxer.hikari.HikariDataSource
import org.kuark.data.jdbc.support.RdbKit
import org.kuark.data.jdbc.metadata.RdbType
import javax.sql.DataSource


object DataSourceKit {

    fun getCurrentDataSource(): DataSource {
        TODO()
    }

    fun getDataSource(dataSourceId: String): DataSource {
        TODO()
    }

    fun createDataSource(
        rdbType: RdbType,
        url: String,
        username: String,
        password: String?,
        catalog: String? = null,
        schema: String? = null
    ): DataSource =
        HikariDataSource().apply {
            jdbcUrl = url
            this.username = username
            this.password = password
            driverClassName = rdbType.jdbcDriverName
            connectionTestQuery = RdbKit.getTestStatement(rdbType)
            this.catalog = catalog
            this.schema = schema
        }


}