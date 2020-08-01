package org.kuark.data.jdbc.datasource

import org.kuark.context.core.KuarkContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

/**
 * 多数据源
 *
 * @author K
 * @since 1.0.0
 */
class MultipleDataSource : AbstractRoutingDataSource() {

    companion object {
        const val DEFAULT_DATASOURCE_ID = "defaultDataSource"
    }

    @Autowired
    private lateinit var dataSourceRouter: IDataSourceRouter

    override fun determineCurrentLookupKey(): Any? {
        var key = KuarkContext.get().dataSourceId
        if (key == null || key.isBlank()) {
            key = dataSourceRouter.determineDataSourceId()
            if (key == null || key.isBlank()) {
                key =  DEFAULT_DATASOURCE_ID
            }
            KuarkContext.get().dataSourceId = key
        }
        return key
    }

}