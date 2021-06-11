package io.kuark.test.service.service1

import io.kuark.test.YamlPropertySourceFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.PropertySource

/**
 * 服务1-节点1
 *
 * @author K
 * @since 1.0.0
 */
@ComponentScan(
    basePackages = [
        "io.kuark.test.service.service1",
    ], //!!! 不能是io.kuark，不然excludeFilters不会生效
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = [Service1Node2Application::class]
    )]
)
@PropertySource(
    value = [
        "classpath:service1-node1.yml"
    ],
    factory = YamlPropertySourceFactory::class
)
@SpringBootApplication
open class Service1Node1Application {

    companion object {

        var context: ConfigurableApplicationContext? = null


        @JvmStatic
        fun main(args: Array<String>) {
            context = SpringApplication.run(Service1Node1Application::class.java, *args)
            println("service1-node1 started")
        }

        @JvmStatic
        fun exit() {
            SpringApplication.exit(context)
            println("service1-node1 shutdown")
        }

    }

}