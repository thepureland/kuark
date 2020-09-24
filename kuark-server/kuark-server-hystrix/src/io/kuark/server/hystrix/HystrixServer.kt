package io.kuark.server.hystrix

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard


/**
 * Hystrix Dashboard服务器，依赖于Eureka
 *
 * @author K
 * @since 1.0.0
 */
@EnableHystrixDashboard
@EnableDiscoveryClient
@SpringBootApplication
open class HystrixServer {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(HystrixServer::class.java, *args)
        }
    }

}