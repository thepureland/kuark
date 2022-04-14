package io.kuark.ability.cache.core

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component


/**
 * 缓存处理器Bean后处理器。用于加载所有需要在启动时加载的缓存数据
 *
 * @author K
 * @since 1.0.0
 */
@Component
class CacheHandlerBeanPostProcessor: BeanPostProcessor {

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean is AbstractCacheHandler<*>) {
            val cacheConfig = CacheKit.getCacheConfig(bean.cacheName())
            if (cacheConfig != null && cacheConfig.writeOnBoot == true) {
                bean.reloadAll(false)
            }
        }
        return bean
    }

}