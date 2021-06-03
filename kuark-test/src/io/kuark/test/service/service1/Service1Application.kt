package io.kuark.test.service.service1

import io.kuark.test.YamlPropertySourceFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.PropertySource

/**
 * 服务1应用
 *
 * @author K
 * @since 1.0.0
 */
//@EnableFeignClients //(basePackages = ["io.kuark"]) // feign如果不是在相同的module 下，就必须加上 自己的扫描范围
//@EnableDiscoveryClient
//@ContextConfiguration(loader = BranchTx1Application.LocalCacheTestContextLoader::class) // 未能替换
//@ComponentScan(
//    basePackages = [
//        "io.kuark.context",
//    ], //!!! 不能是io.kuark，不然excludeFilters不会生效
//    excludeFilters = [ComponentScan.Filter(
//        type = FilterType.REGEX,
//        pattern = ["io\\.kuark\\.ability\\.distributed\\.gateway\\.service2\\.*"]
//    )]
//)
@PropertySource(
    value = [
        "classpath:service1.yml"
    ],
    factory = YamlPropertySourceFactory::class
)
@SpringBootApplication
open class Service1Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Service1Application::class.java, *args)
        }
    }

}