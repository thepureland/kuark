package org.kuark.data.cache.core

import java.io.Serializable

class CacheMessage(var cacheName: String, var key: Any?) : Serializable {

    companion object {
        /**  */
        private const val serialVersionUID = 5987219310442078193L
    }

}