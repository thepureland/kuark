package io.kuark.ability.distributed.discovery

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaAutoServiceRegistration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType


/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
open class EurekaClientTest {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(EurekaClientTest::class.java, *args)
        }
    }

}