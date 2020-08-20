package io.kuark.cache.core

import java.io.Serializable

/**
 * 缓存通知消息对象
 *
 * @author K
 * @since 1.0.0
 */
open class CacheMessage() : Serializable {

    lateinit var cacheName: String
    var key: Any? = null

    constructor(cacheName: String, key: Any?) : this() {
        this.cacheName = cacheName
        this.key = key
    }

    companion object {
        /**  */
        private const val serialVersionUID = 5987219310442078193L
    }

}