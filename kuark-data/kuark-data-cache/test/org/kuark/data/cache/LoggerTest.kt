package org.kuark.data.cache

import org.junit.jupiter.api.Test
import org.kuark.base.log.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
//@SpringBootApplication
open class LoggerTest {

    val logger = LoggerFactory.getLogger(LoggerTest::class)

    @Test
    fun test() {
        logger.info("kkkkkkkkkkkkk")
    }

}
