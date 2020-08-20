package io.kuark.data.jdbc.datasource

/**
 * 数据源路由器接口
 *
 * @author K
 * @since 1.0.0
 */
interface IDataSourceRouter {

    fun determineDataSourceId(): String?

}