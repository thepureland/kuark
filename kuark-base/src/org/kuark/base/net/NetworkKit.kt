package org.kuark.base.net

import java.net.Socket

/**
 * 网络工具
 */
object NetworkKit {

    const val LOCALHOST_IP = "127.0.0.1"
    const val ANYHOST_IP = "0.0.0.0"

    /**
     * 判断端口是否启用
     * @param ip ip地址
     * @param port 端口号
     */
    fun isPortActive(ip: String, port: Int): Boolean {
        try {
            Socket(ip, port).close()
            return true
        } catch (e: Exception) {
            return false
        }
    }

}