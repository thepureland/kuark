package io.kuark.context.spring

import io.kuark.context.annotation.ConfigValue
import io.kuark.test.SpringTest
import org.junit.jupiter.api.Test


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