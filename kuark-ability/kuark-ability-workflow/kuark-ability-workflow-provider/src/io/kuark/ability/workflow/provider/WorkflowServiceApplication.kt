package io.kuark.ability.workflow.provider

import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

@SpringBootApplication(scanBasePackages = ["io.kuark"], exclude = [SecurityAutoConfiguration::class, ManagementWebSecurityAutoConfiguration::class])
open class WorkflowServiceApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(WorkflowServiceApplication::class.java, *args)
        }
    }

}