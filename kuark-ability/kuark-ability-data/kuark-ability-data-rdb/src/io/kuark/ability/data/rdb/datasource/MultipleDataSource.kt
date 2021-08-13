package io.kuark.ability.data.rdb.datasource

import io.kuark.base.lang.string.StringKit
import io.kuark.context.core.KuarkContextHolder
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
        var key = KuarkContextHolder.get().dataSourceId
        if (StringKit.isBlank(key)) {
            key = dataSourceRouter.determineDataSourceId()
            if (StringKit.isBlank(key)) {
                key =  DEFAULT_DATASOURCE_ID
            }
            KuarkContextHolder.get().dataSourceId = key
        }
        return key
    }

}