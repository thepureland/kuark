package org.kuark.data.cache

import org.junit.jupiter.api.Test
import org.kuark.base.log.LoggerFactory
import org.kuark.data.cache.context.CacheNames
import org.kuark.data.cache.core.MixCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@SpringBootTest
@SpringBootApplication
open class MixCacheTest {

    @Autowired
    private lateinit var testService: TestService

    @Autowired
    private lateinit var cacheManager: CacheManager

    private val logger = LoggerFactory.getLogger(LoggerTest::class)

    @Test
    fun test1() {
        Thread(Runnable {
            var v = testService.getFromDB("1")
            v = testService.getFromDB("1")
            v = testService.getFromDB("1")
            logger.info("----------验证缓存是否生效----------")
            val cache = cacheManager.getCache(CacheNames.DEMO)
            logger.info(cache.toString())
            logger.info(cache?.get("1", String::class.java).toString())
        }).start()
        Thread.sleep(1000000)
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

