package org.kuark.data.jdbc.datasource

import org.kuark.base.lang.string.StringKit
import org.kuark.config.context.RequestContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource


class MultipleDataSource : AbstractRoutingDataSource() {

    companion object {
        const val DEFAULT_DATASOURCE_ID = "defaultDataSource"
    }

    @Autowired
    private lateinit var dataSourceRouter: IDataSourceRouter

    override fun determineCurrentLookupKey(): Any? {
        var key = RequestContext.get().dataSourceId
        if (StringKit.isBlank(key)) {
            key = dataSourceRouter.determineDataSourceId()
            if (StringKit.isBlank(key)) {
                key =  DEFAULT_DATASOURCE_ID
            }
            RequestContext.get().dataSourceId = key
        }
        return key
    }

}