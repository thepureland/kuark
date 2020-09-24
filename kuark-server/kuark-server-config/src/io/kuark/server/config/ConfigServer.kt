package io.kuark.server.config

import org.springframework.boot.SpringApplication

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.config.server.EnableConfigServer


/**
 * spring cloud配置中心，依赖于注册中心
 *
 * @author K
 * @since 1.0.0
 */
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
open class ConfigServer {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ConfigServer::class.java, *args)
        }
    }

}