package org.kuark.config

import org.junit.jupiter.api.Test
import org.kuark.config.annotation.ConfigValue
import org.kuark.config.spring.SpringKit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
@SpringBootApplication
//@ComponentScan("org.kuark.config")
open class SpringTest {

    @ConfigValue(value = "\${nacos.test:123}", autoRefreshed = true)
    private val testProperties: String? = null

    @Test
    fun contextLoads() {
        println("############  contextLoads ")
        println(SpringKit.getProperty("nacos.config.server-addr"))
    }

    @Test
    fun test() {
        println("############  test: $testProperties")
    }


}