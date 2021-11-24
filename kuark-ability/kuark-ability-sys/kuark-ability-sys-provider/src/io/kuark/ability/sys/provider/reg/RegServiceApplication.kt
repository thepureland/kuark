package io.kuark.ability.sys.provider.reg

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["io.kuark"])
open class RegServiceApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(RegServiceApplication::class.java, *args)
        }
    }

}

