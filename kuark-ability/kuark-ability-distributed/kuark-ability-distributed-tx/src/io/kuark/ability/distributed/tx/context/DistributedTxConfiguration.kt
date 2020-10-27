package io.kuark.ability.distributed.tx.context

import io.seata.rm.datasource.DataSourceProxy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Configuration
open class DistributedTxConfiguration {

    @Bean
    open fun dataSourceProxy(dataSource: DataSource): DataSourceProxy {
        return DataSourceProxy(dataSource)
    }

}