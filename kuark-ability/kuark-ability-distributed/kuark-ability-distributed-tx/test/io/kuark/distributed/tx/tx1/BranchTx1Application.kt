package io.kuark.distributed.tx.tx1

import io.kuark.context.spring.YamlPropertySourceFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.PropertySource
import java.util.*

/**
 * 分支事务1应用
 *
 * @author K
 * @since 1.0.0
 */
//@EnableFeignClients //(basePackages = ["io.kuark"]) // feign如果不是在相同的module 下，就必须加上 自己的扫描范围
//@EnableDiscoveryClient
//@ContextConfiguration(loader = BranchTx1Application.LocalCacheTestContextLoader::class) // 未能替换
@ComponentScan(
    basePackages = [
        "io.kuark.context",
        "io.kuark.ability.data.rdb",
        "io.kuark.ability.distributed.tx.context",
        "io.kuark.distributed.tx.table",
        "io.kuark.distributed.tx.tx1",
    ], //!!! 不能是io.kuark，不然excludeFilters不会生效
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ["io\\.kuark\\.distributed\\.tx\\.tx\\.*", "io\\.kuark\\.distributed\\.tx\\.tx2\\.*"]
    )]
)
@PropertySource(
    value = [
        "classpath:application-ability-data-rdb-default.yml",
        "classpath:application-ability-distributed-client-default.yml",
        "classpath:tx1.yml"
    ],
    factory = YamlPropertySourceFactory::class
)
@SpringBootApplication
open class BranchTx1Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            val stream = BranchTx1Application::class.java.classLoader.getResourceAsStream("tx1.yml")
//            val properties = Properties()
//            properties.load(stream)
//            val app = SpringApplication(BranchTx1Application::class.java)
//            app.setDefaultProperties(properties)
//            app.run(*args)

            SpringApplication.run(BranchTx1Application::class.java, *args)
        }
    }

//    class LocalCacheTestContextLoader : TestSpringBootContextLoader() {
//        override fun getDynamicProperties(): Map<String, String> {
//            return mapOf("server.port" to "8181", "spring.application.name" to "branch-tx1")
//        }
//    }

}