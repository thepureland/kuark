package io.kuark.data.jdbc.datasource

import com.zaxxer.hikari.HikariDataSource
import io.kuark.context.core.KuarkContextHolder
import io.kuark.data.jdbc.support.RdbKit
import javax.sql.DataSource

/**
 * 数据源工具类
 *
 * @author K
 * @since 1.0.0
 */
object DataSourceKit {

    fun getCurrentDataSource(): DataSource = KuarkContextHolder.currentDataSource()

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