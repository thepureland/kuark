package io.kuark.test.server.eureka

import io.kuark.base.lang.SystemKit
import io.kuark.base.net.NetworkKit
import io.kuark.test.YamlPropertySourceFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource


/**
 * Eureka注册中心
 *
 * @author K
 * @since 1.0.0
 */
@ComponentScan(
    basePackages = [
        "io.kuark.test.server.eureka",
    ], //!!! 不能是io.kuark，不然excludeFilters不会生效
    excludeFilters = [ComponentScan.Filter(
    )]
)
@PropertySource(
    value = [
        "classpath:eureka-server.yml"
    ],
    factory = YamlPropertySourceFactory::class
)
@EnableEurekaServer
@SpringBootApplication(excludeName = ["org.springframework.cloud.gateway.config.GatewayRedisAutoConfiguration"])
open class EurekaServer {

    companion object {

        private var context: ConfigurableApplicationContext? = null

        @JvmStatic
        fun main(args: Array<String>) {
            if (!NetworkKit.isPortActive("localhost", 8001)) {
                SystemKit.setEnvVars(mapOf(
                    "spring.main.web-application-type" to "servlet",
                ))
                context = SpringApplication.run(EurekaServer::class.java, *args)
                println("eureka server UP")
            }
        }

        @JvmStatic
        fun exit() {
            if (context != null && context!!.isRunning) {
                SpringApplication.exit(context)
                println("eureka server DOWN")
            }
        }

    }

}