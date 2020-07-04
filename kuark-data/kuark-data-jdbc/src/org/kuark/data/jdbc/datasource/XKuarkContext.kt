package org.kuark.data.jdbc.datasource

import org.kuark.config.context.KuarkContext
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

fun KuarkContext.currentDataSource(): DataSource = this.get().otherInfos["DATA_SOURCE"] as DataSource