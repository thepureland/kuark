package io.kuak.ability.distributed.config

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
open class ConfigClientApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ConfigClientApplication::class.java, *args)
        }
    }

}