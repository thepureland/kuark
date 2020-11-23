package io.kuark.server.eureka

import org.springframework.boot.SpringApplication

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer


/**
 * 启动Eureka注册中心
 *
 * @author K
 * @since 1.0.0
 */
@EnableEurekaServer
@SpringBootApplication
open class EurekaServer {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(EurekaServer::class.java, *args)
        }
    }

}