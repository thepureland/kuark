package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.cache.support.AbstractCacheManager
import io.kuark.base.lang.GenericKit
import io.kuark.base.log.LogFactory
import io.kuark.context.kit.SpringKit
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/sys/cache/management")
@CrossOrigin
class CacheManagementController {

    private val cacheMgmtMap = mutableMapOf<String, AbstractCacheManager<*>>()

    private val log = LogFactory.getLog(this::class)

    @GetMapping("/reload")
    fun reload(cacheName: String, key: String): String {
        getCacheMgmtBean(cacheName).reload(key)
        return "重载Key为${key}的缓存成功！"
    }

    @GetMapping("/reloadAll")
    fun reloadAll(cacheName: String): String {
        getCacheMgmtBean(cacheName).reloadAll(true)
        return "重载缓存成功！"
    }

    @GetMapping("/evict")
    fun evict(cacheName: String, key: String): String {
        getCacheMgmtBean(cacheName).evict(key)
        return "踢除Key为${key}的缓存成功！"
    }

    @GetMapping("/clear")
    fun clear(cacheName: String): String {
        getCacheMgmtBean(cacheName).clear()
        return "缓存清除成功！"
    }

    @GetMapping("/isExists")
    fun isExists(cacheName: String, key: String): String {
        val exists = getCacheMgmtBean(cacheName).isExists(key)
        return if (exists) "缓存Key: ${key}存在！" else "缓存Key: ${key}不存在！"
    }

    @GetMapping("/valueInfo")
    fun valueInfo(cacheName: String, key: String): String {
        val cacheMgmt = getCacheMgmtBean(cacheName)
        val valueClass = GenericKit.getSuperClassGenricClass(cacheMgmt::class, 0)
        val value = cacheMgmt.value(key)
        var msg = "Key：${key}对应的值类型为${valueClass}"
        return when (value) {
            null -> "${msg}，值为null"
            is Collection<*> -> "${msg}，元素个数为${value.size}"
            is Map<*, *> -> "${msg}，元素个数为${value.size}"
            is Array<*> -> "${msg}，元素个数为${value.size}"
            else -> msg
        }
    }

    private fun getCacheMgmtBean(cacheName: String): AbstractCacheManager<*> {
        if (cacheMgmtMap.isEmpty()) {
            val beanMap = SpringKit.getBeansOfType(AbstractCacheManager::class)
            beanMap.values.forEach {
                cacheMgmtMap[it.cacheName()] = it
            }
        }
        val cacheMgmt = cacheMgmtMap[cacheName]
        if (cacheMgmt == null) {
            val errMsg = "不存在名称为${cacheName}的缓存！"
            log.error(errMsg)
            throw IllegalArgumentException(errMsg)
        }
        return cacheMgmt
    }

}