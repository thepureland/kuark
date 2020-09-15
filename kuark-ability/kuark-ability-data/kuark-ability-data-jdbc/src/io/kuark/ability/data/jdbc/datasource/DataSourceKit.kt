package io.kuark.ability.data.jdbc.datasource

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

    /**
     * 返回当前数据源
     *
     * @return 数据源
     * @author K
     * @since 1.0.0
     */
    fun getCurrentDataSource(): DataSource = KuarkContextHolder.currentDataSource()

    fun getDataSource(dataSourceId: String): DataSource {
        TODO()
    }

    /**
     * 创建数据源
     *
     * @param url 连接地址
     * @param username 用户名
     * @param password 用户密码
     * @param catalog Catalog
     * @param schema Schema
     * @return 数据源
     * @author K
     * @since 1.0.0
     */
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