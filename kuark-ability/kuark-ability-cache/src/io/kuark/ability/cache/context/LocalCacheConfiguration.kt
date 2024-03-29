package io.kuark.ability.cache.context

import com.github.benmanes.caffeine.cache.Caffeine
import io.kuark.ability.cache.support.ICacheConfigProvider
import io.kuark.base.log.LogFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import java.util.concurrent.TimeUnit

/**
 * 本地缓存(第一级缓存)springboot配置类
 *
 * @author K
 * @since 1.0.0
 */
@SpringBootConfiguration
@ConditionalOnBean(MixCacheConfiguration::class)
//@ConditionalOnExpression("'\${cache.config.strategy}'.equals('SINGLE_LOCAL') || '\${cache.config.strategy}'.equals('LOCAL_REMOTE')")
open class LocalCacheConfiguration {

//    @Value("\${spring.cache.caffeine.spec}")
//    private lateinit var caffeineSpec: String

    private val log = LogFactory.getLog(this::class)

    @Bean(name = ["localCacheManager"])
    open fun localCacheManager(cacheConfigProvider: ICacheConfigProvider): CacheManager {
//        val cacheManager = CaffeineCacheManager()
////        val spec = CaffeineSpec.parse(caffeineSpec)
////        val caffeine = Caffeine.from(spec)
////        cacheManager.setCaffeine(caffeine)

        val cacheManager = SimpleCacheManager()
        val localCacheConfigs = cacheConfigProvider.getLocalCacheConfigs()
        val localRemoteCacheConfigs = cacheConfigProvider.getLocalRemoteCacheConfigs()
        val cacheConfigs = localCacheConfigs.plus(localRemoteCacheConfigs)
        val caches = cacheConfigs.map {
            log.info("初始化本地缓存【${it.key}】...")
            val cacheBuilder = Caffeine.newBuilder()
            if (it.value.ttl != null) {
                cacheBuilder.expireAfterWrite(it.value.ttl!!.toLong(), TimeUnit.SECONDS)
            }
            val caffeineCache = CaffeineCache(it.key, cacheBuilder.build())
            log.info("初始化本地缓存【${it.key}】成功！")
            caffeineCache
        }
        cacheManager.setCaches(caches)
        return cacheManager
    }

}