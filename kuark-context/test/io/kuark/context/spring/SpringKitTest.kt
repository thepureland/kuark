package io.kuark.context.spring

import io.kuark.test.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value


internal class SpringKitTest : SpringTest() {

    @Value(value = "\${nacos.test:123}")
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