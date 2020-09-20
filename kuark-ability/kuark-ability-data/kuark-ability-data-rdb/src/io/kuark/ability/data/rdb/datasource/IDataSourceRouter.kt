package io.kuark.ability.data.rdb.datasource

/**
 * 数据源路由器接口
 *
 * @author K
 * @since 1.0.0
 */
interface IDataSourceRouter {

    /**
     * 决定数据源id
     *
     * @return 数据源id
     * @author K
     * @since 1.0.0
     */
    fun determineDataSourceId(): String?

}