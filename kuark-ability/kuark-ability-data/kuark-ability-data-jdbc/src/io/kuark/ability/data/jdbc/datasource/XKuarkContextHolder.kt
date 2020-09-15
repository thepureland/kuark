package io.kuark.ability.data.jdbc.datasource

import io.kuark.context.core.KuarkContextHolder
import io.kuark.context.spring.SpringKit
import javax.sql.DataSource

/**
 * KuarkContextHolder的扩展函数
 *
 * @author K
 * @since 1.0.0
 */

/**
 * 设置当前线程关联的数据源
 *
 * @param dataSource 数据源
 * @author K
 * @since 1.0.0
 */
fun KuarkContextHolder.setCurrentDataSource(dataSource: DataSource) = run {
    this.get().otherInfos["DATA_SOURCE"] = dataSource
}

/**
 * 返回当前线程关联的数据源，如果没有，取默认数据源，并将其与当前线程关联
 *
 * @return 数据源
 * @author K
 * @since 1.0.0
 */
fun KuarkContextHolder.currentDataSource(): DataSource {
    var dataSource = this.get().otherInfos["DATA_SOURCE"]
    if (dataSource == null) {
        dataSource = SpringKit.getBean("dataSource")
        setCurrentDataSource(dataSource as DataSource)
    }
    return dataSource as DataSource
}