package io.kuark.distributed.tx.tx2

import io.kuark.context.spring.YamlPropertySourceFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.PropertySource

/**
 * 分支事务2应用
 *
 * @author K
 * @since 1.0.0
 */
@EnableFeignClients //(basePackages = ["io.kuark"]) // feign如果不是在相同的module 下，就必须加上 自己的扫描范围
@EnableDiscoveryClient
@PropertySource(
    value = ["classpath:application-ability-data-rdb-default.yml",
        "classpath:application-ability-distributed-client-default.yml",
        "classpath:tx2.yml"],
    factory = YamlPropertySourceFactory::class
)
@ComponentScan(
    basePackages = [
        "io.kuark.context",
        "io.kuark.ability.data.rdb",
        "io.kuark.ability.distributed.tx.context",
        "io.kuark.distributed.tx.table",
        "io.kuark.distributed.tx.tx2"
    ], //!!! 不能是io.kuark，不然excludeFilters不会生效
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ["io\\.kuark\\.distributed\\.tx\\.tx\\.*", "io\\.kuark\\.distributed\\.tx\\.tx1\\.*"]
    )]
)
@SpringBootApplication
open class BranchTx2Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            val inputStream = BranchTx2Application::class.java.classLoader.getResourceAsStream("tx2.yml")
//            val properties = Properties()
//            properties.load(inputStream)
//            val app = SpringApplication(BranchTx2Application::class.java)
//            app.setDefaultProperties(properties)
//            app.run(*args)

            SpringApplication.run(BranchTx2Application::class.java, *args)
        }
    }

}