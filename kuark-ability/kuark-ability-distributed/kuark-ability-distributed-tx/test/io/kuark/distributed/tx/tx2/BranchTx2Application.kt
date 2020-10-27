package io.kuark.distributed.tx.tx2

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
open class BranchTx2Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(BranchTx2Application::class.java, *args)
        }
    }

}