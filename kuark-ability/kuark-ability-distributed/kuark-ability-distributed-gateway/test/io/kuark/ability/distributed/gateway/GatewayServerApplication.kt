package io.kuark.ability.distributed.gateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
open class GatewayServerApplication {

    companion object {
        @JvmStatic
        open fun main(args: Array<String>) {
            SpringApplication.run(GatewayServerApplication::class.java, *args)
        }
    }

}