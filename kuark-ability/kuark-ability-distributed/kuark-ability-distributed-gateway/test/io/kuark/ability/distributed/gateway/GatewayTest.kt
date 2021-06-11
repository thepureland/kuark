package io.kuark.ability.distributed.gateway

import io.kuark.context.spring.YamlPropertySourceFactory
import io.kuark.test.SpringTest
import io.kuark.test.service.service1.Service1Node1Application
import io.kuark.test.service.service1.Service1Node2Application
import org.junit.jupiter.api.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration
import org.springframework.context.annotation.PropertySource

//@SpringBootTest
@SpringBootApplication(exclude = [GatewayAutoConfiguration::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal open class GatewayTest {

    @BeforeAll
    fun setUp() {
//        Service1Node1Application.main(arrayOf())
//        Service1Node2Application.main(arrayOf())

//        val subThread = Thread {
//            Service1Node1Application.main(arrayOf())
//            println("kkkk")
//            Service1Node2Application.main(arrayOf())
//        }
//        subThread.start()
//        subThread.join()
    }

    @AfterAll
    fun tearDown() {
//        Service1Node1Application.exit()
//        Service1Node2Application.exit()
    }

    @Test
    fun getTablesByType() {
        println("dkjsdkjfdskfjks")
    }

}