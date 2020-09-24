package io.kuark.ability.cache.core

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.cache.enums.CacheStrategy
import io.kuark.test.SpringTest
import io.kuark.test.TestSpringBootContextLoader
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.CountDownLatch


/**
 * 远程缓存测试用例
 *
 * @author K
 * @since 1.0.0
 */
@ContextConfiguration(loader = RemoteCacheTest.RemoteCacheTestContextLoader::class)
internal class RemoteCacheTest : SpringTest() {

    @Autowired
    private lateinit var cacheTestService: CacheTestService

    @Autowired(required = false)
    @Qualifier("localCacheManager")
    private lateinit var localCacheManager: CacheManager

    @Autowired(required = false)
    @Qualifier("remoteCacheManager")
    private lateinit var remoteCacheManager: CacheManager

    @Autowired(required = false)
    private lateinit var mixCacheManager: MixCacheManager

    @Test
    fun testRemoteCache() {
        assertThrows<UninitializedPropertyAccessException> { localCacheManager }

        val remoteCache = remoteCacheManager.getCache(CacheNames.TEST)
        assert(remoteCache != null)
        val mixCache = mixCacheManager.getCache(CacheNames.TEST)
        assert(mixCache != null)

        val latch = CountDownLatch(1)
        Thread{
            val key = "remote_key"

            val value1 = cacheTestService.getFromDB(key)
            val value2 = cacheTestService.getFromDB(key)
            assert(value1 == value2)

            val value3 = remoteCache!!.get(key, String::class.java)
            val value4 = mixCache!!.get(key, String::class.java)
            assert(value3 == value4)
            assert(value3 == value2)

            latch.countDown()
        }.start()
        latch.await()
    }

    class RemoteCacheTestContextLoader : TestSpringBootContextLoader() {

        override fun getDynamicProperties(): Map<String, String> {
            return mapOf("cache.config.strategy" to CacheStrategy.REMOTE.name)
        }

    }

}

