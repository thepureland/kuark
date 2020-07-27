package org.kuark.data.jdbc.datasource

import org.kuark.config.context.KuarkContext
import org.kuark.config.spring.SpringKit
import javax.sql.DataSource

/**
 * KuarkContext的扩展函数
 *
 * @author K
 * @since 1.0.0
 */

fun KuarkContext.setCurrentDataSource(dataSource: DataSource) = run {
    this.get().otherInfos["DATA_SOURCE"] = dataSource
}

fun KuarkContext.currentDataSource(): DataSource {
    var dataSource = this.get().otherInfos["DATA_SOURCE"]
    if (dataSource == null) {
        dataSource = SpringKit.getBean("dataSource")
        setCurrentDataSource(dataSource as DataSource)
    }
    return dataSource as DataSource
}