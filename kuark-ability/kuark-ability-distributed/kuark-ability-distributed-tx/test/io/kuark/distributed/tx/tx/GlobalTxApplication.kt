package io.kuark.distributed.tx.tx

import io.kuark.context.spring.YamlPropertySourceFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.PropertySource

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(
    basePackages = [
        "io.kuark.context",
        "io.kuark.ability.data.rdb",
        "io.kuark.ability.distributed.tx.context",
        "io.kuark.distributed.tx.table",
        "io.kuark.distributed.tx.tx"
    ], //!!! 不能是io.kuark，不然excludeFilters不会生效
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ["io\\.kuark\\.distributed\\.tx\\.tx1\\.*", "io\\.kuark\\.distributed\\.tx\\.tx2\\.*"]
    )]
)
@PropertySource(
    "classpath:application-ability-data-rdb-default.yml",
    "classpath:application-ability-distributed-client-default.yml",
    "classpath:tx.yml",
    factory = YamlPropertySourceFactory::class
)
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
open class GlobalTxApplication