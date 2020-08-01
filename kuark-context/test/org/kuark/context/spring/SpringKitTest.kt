package org.kuark.config.spring

import org.junit.jupiter.api.Test
import org.kuark.config.annotation.ConfigValue
import org.kuark.config.spring.SpringKit
import org.kuark.test.SpringTest


internal class SpringKitTest : SpringTest() {

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