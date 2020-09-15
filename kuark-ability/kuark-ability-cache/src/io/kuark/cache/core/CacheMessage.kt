package io.kuark.cache.core

import java.io.Serializable

/**
 * 缓存通知消息对象
 *
 * @author K
 * @since 1.0.0
 */
open class CacheMessage() : Serializable {

    /** 缓存名称 */
    lateinit var cacheName: String

    /** 缓存key */
    var key: Any? = null

    constructor(cacheName: String, key: Any?) : this() {
        this.cacheName = cacheName
        this.key = key
    }

}