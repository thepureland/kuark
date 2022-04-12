package io.kuark.ability.cache.core

import io.kuark.base.log.LogFactory
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.io.Serializable
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch

/**
 * 批量缓存注解测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class BatchCacheableTest : SpringTest() {

    @Autowired
    private lateinit var testCacheService: TestCacheService

    @Test
    fun test() {
        val latch = CountDownLatch(1)
        Thread {
            doTest()
            latch.countDown()
        }.start()
        latch.await()
    }

    private fun doTest() {
        // 加载并缓存单条数据
        val obj1 = testCacheService.load("1", 2, "5", true, 7).first()
        val cachedObj1 = testCacheService.load("1", 2, "5", true, 7).first()
        assert(obj1.time == cachedObj1.time)

        // 加载并缓存单条数据的另一种方式
        val obj2 = testCacheService.batchLoad("1", listOf(2), arrayOf("6"), true, 7).values.first().first()
        val cachedObj2 = testCacheService.batchLoad("1", listOf(2), arrayOf("6"), true, 7).values.first().first()
        assert(obj2.time == cachedObj2.time)

        // 批量加载
        val map = testCacheService.batchLoad("1", listOf(2, 3, 4), arrayOf("5", "6"), true, 7)
        assert(map.size == 6)
        assert(map["1:2:5:7"]!!.first().time == obj1.time) // 不是刚加载的，之前已缓存过
        assert(map["1:2:6:7"]!!.first().time == obj2.time) // 不是刚加载的，之前已缓存过
        assert(map["1:2:6:7"]!!.first().time != map["1:3:6:7"]!!.first().time)
        assert(map["1:3:6:7"]!!.first().time == map["1:4:5:7"]!!.first().time)
        assert(map["1:4:5:7"]!!.first().time == map["1:3:5:7"]!!.first().time)
        assert(map["1:3:5:7"]!!.first().time == map["1:4:6:7"]!!.first().time)
    }

    @Service
    @CacheConfig(cacheNames = ["test"])
    open class TestCacheService {

        private val log = LogFactory.getLog(this::class)

        private var allData = listOf(
            TestCacheObject("1", 2, "5", null, 7),
            TestCacheObject("1", 3, "6", null, 7),
            TestCacheObject("1", 4, "5", null, 7),
            TestCacheObject("1", 2, "6", null, 7),
            TestCacheObject("1", 3, "5", null, 7),
            TestCacheObject("1", 4, "6", null, 7)
        )

        @Cacheable(key = "#module.concat('::').concat(#age).concat('::').concat(#name).concat('::').concat(#type)")
        open fun load(module: String, age: Int, name: String, active: Boolean, type: Int): List<TestCacheObject> {
            log.debug("单条加载数据，参数：$module, $age, $name, $type")
            Thread.sleep(1000) // 模拟耗时的io操作
            val data = allData.filter { it.module == module && it.age == age && it.name == name && it.type == type }
            val result = mutableListOf<TestCacheObject>()
            val now = LocalDateTime.now()
            data.forEach {
                val another = it.copy()
                another.time = now
                result.add(another)
            }
            return result
        }

        @BatchCacheable(valueClass = List::class, ignoreParamIndexes = [3])
        open fun batchLoad(
            module: String, ages: List<Int>, names: Array<String>, active: Boolean, type: Int
        ): Map<String, List<TestCacheObject>> {
            log.debug("批量加载数据，参数：$module, $ages, ${names.toList()}, $type")
            Thread.sleep(1000) // 模拟耗时的io操作
            val list = allData.filter { it.module == module && it.age in ages && it.name in names && it.type == type }
            val result = linkedMapOf<String, List<TestCacheObject>>()
            val now = LocalDateTime.now()
            list.forEach {
                val another = it.copy()
                another.time = now
                result["${it.module}::${it.age}::${it.name}::${it.type}"] = listOf(another)
            }
            return result
        }

    }

    data class TestCacheObject(
        val module: String,
        val age: Int,
        val name: String,
        var time: LocalDateTime?,
        val type: Int
    ) : Serializable

}