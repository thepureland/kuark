package io.kuark.ability.distributed.gateway

import io.kuark.base.lang.SystemKit
import io.kuark.base.net.NetworkKit
import io.kuark.context.spring.YamlPropertySourceFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.PropertySource


@PropertySource(
    value = [
        "classpath:application-ability-distributed-gateway-junit.yml"
    ],
    factory = YamlPropertySourceFactory::class
)
@SpringBootApplication
open class GatewayServerApplication {

    companion object {

        private var context: ConfigurableApplicationContext? = null

        @JvmStatic
        open fun main(args: Array<String>) {
            if (!NetworkKit.isPortActive("localhost", 9201)) {
                SystemKit.setEnvVars(mapOf(
                    "spring.main.web-application-type" to "reactive",
                    "spring.cloud.gateway.enabled" to "true"
                ))
                context = SpringApplication.run(GatewayServerApplication::class.java, *args)
                println("gateway server UP")
            }
        }

        @JvmStatic
        fun exit() {
            if (context != null && context!!.isRunning) {
                SpringApplication.exit(context)
                println("gateway server DOWN")
            }
        }

    }

}