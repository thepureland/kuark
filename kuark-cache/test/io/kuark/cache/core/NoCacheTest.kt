package io.kuark.cache.core

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.kuark.test.SpringTest
import io.kuark.test.TestSpringBootContextLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.test.context.ContextConfiguration


/**
 * 本地缓存测试用例
 *
 * @author K
 * @since 1.0.0
 */
@ContextConfiguration(loader = NoCacheTest.NoCacheTestContextLoader::class)
internal class NoCacheTest : SpringTest() {

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
    fun testLocalCache() {
        assertThrows<UninitializedPropertyAccessException> { localCacheManager }
        assertThrows<UninitializedPropertyAccessException> { remoteCacheManager }
        assertThrows<UninitializedPropertyAccessException> { mixCacheManager }

        val key = "key"
        val value1 = cacheTestService.getFromDB(key)
        val value2 = cacheTestService.getFromDB(key)
        assert(value1 != value2)
    }

    class NoCacheTestContextLoader : TestSpringBootContextLoader() {

        override fun getDynamicProperties(): Map<String, String> {
            return mapOf("cache.config.enabled" to "false")
        }

    }

}

