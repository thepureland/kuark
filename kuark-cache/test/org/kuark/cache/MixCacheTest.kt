package org.kuark.cache

import org.junit.jupiter.api.Test
import org.kuark.base.log.LogFactory
import org.kuark.cache.context.CacheNames
import org.kuark.cache.kit.CacheKit
import org.kuark.config.spring.JunitApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

/**
 * 混合缓存(两级缓存: 本地+远程)测试用例
 *
 * @author K
 * @since 1.0.0
 */
@SpringBootTest(classes = [JunitApplication::class])
open class MixCacheTest {

    @Autowired
    private lateinit var testService: TestService

    private val logger = LogFactory.getLog(MixCacheTest::class)

    @Test
    fun test() {
        val latch = CountDownLatch(1)
        Thread{
            var v = testService.getFromDB("1")
            v = testService.getFromDB("1")
            v = testService.getFromDB("1")
            logger.debug("----------验证缓存是否生效----------")
            logger.debug(CacheKit.getValue(CacheNames.DEMO,"1", String::class))
            latch.countDown()
        }.start()
        latch.await()
    }

    @Service
    open class TestService {

        @Cacheable(value = [CacheNames.DEMO], key = "#id")
        open fun getFromDB(id: String): Any? {
            println("模拟去db查询~~~$id")
            return "hello cache..."
        }
    }

}

