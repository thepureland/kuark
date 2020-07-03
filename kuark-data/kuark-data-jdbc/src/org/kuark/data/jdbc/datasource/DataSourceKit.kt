package org.kuark.data.jdbc.datasource

import com.zaxxer.hikari.HikariDataSource
import org.kuark.base.lang.string.startsWithAny
import org.kuark.config.context.KuarkContext
import org.kuark.data.jdbc.support.RdbKit
import org.kuark.data.jdbc.metadata.RdbType
import javax.sql.DataSource


object DataSourceKit {

    fun getCurrentDataSource(): DataSource = KuarkContext.currentDataSource()

    fun getDataSource(dataSourceId: String): DataSource {
        TODO()
    }

    fun createDataSource(
        url: String,
        username: String,
        password: String?,
        catalog: String? = null,
        schema: String? = null
    ): DataSource {
        val rdbType = RdbKit.determinRdbTypeByUrl(url)
        return HikariDataSource().apply {
            jdbcUrl = url
            this.username = username
            this.password = password
            driverClassName = rdbType.jdbcDriverName
            connectionTestQuery = RdbKit.getTestStatement(rdbType)
            catalog?.let { this.catalog = catalog }
            schema?.let { this.schema = schema }
        }
    }



}