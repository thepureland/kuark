package io.kuark.ability.distributed.tx.context

import com.zaxxer.hikari.HikariDataSource
import io.kuark.context.spring.SpringKit
import io.seata.rm.datasource.DataSourceProxy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Configuration
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
open class DistributedTxConfiguration {

////    @Autowired

//    @Value("\${spring.datasource.url}")
//    private lateinit var dbUrl: String



//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    @ConditionalOnMissingBean
//    open fun hakariDataSource(): HikariDataSource {
//        return DataSourceBuilder.create().type(HikariDataSource::class.java)
//            .url(dbUrl) // Hikari直接取的连接地址参数是jdbc-url而不是url
//            .build()
//    }

    @Bean
    @Qualifier(value = "dataSource")
    @Primary
//    @ConditionalOnMissingBean
    open fun dataSource(hakariDataSource: HikariDataSource): DataSource {
        return DataSourceProxy(hakariDataSource)
    }

}