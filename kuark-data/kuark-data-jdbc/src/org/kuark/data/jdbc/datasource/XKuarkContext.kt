package org.kuark.data.jdbc.datasource

import org.kuark.config.context.KuarkContext
import javax.sql.DataSource


fun KuarkContext.setCurrentDataSource(dataSource: DataSource) = run {
    this.get().otherInfos["DATA_SOURCE"] = dataSource
}

fun KuarkContext.currentDataSource(): DataSource = this.get().otherInfos["DATA_SOURCE"] as DataSource