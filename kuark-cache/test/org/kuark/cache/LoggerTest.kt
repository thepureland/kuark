package org.kuark.cache

import org.junit.jupiter.api.Test
import org.kuark.base.log.LogFactory
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
//@SpringBootApplication
open class LoggerTest {

    val logger = LogFactory.getLog(LoggerTest::class)

    @Test
    fun test() {
        logger.info("kkkkkkkkkkkkk")
    }

}
