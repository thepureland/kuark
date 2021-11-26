package io.kuark.demo.console.single

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["io.kuark"])
open class ConsoleSingleApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ConsoleSingleApplication::class.java, *args)
        }
    }

}