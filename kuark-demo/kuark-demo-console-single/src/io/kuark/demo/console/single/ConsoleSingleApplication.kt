package io.kuark.demo.console.single

import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

@SpringBootApplication(scanBasePackages = ["io.kuark"], exclude = [SecurityAutoConfiguration::class, ManagementWebSecurityAutoConfiguration::class])
open class ConsoleSingleApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ConsoleSingleApplication::class.java, *args)
        }
    }

}