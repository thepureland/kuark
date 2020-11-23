package io.kuark.server.nacos

import com.alibaba.nacos.Nacos

/**
 * 启动Nacos Server及其web控制台
 *
 * @author K
 * @since 1.0.0
 */
fun main(args: Array<String>) {
    System.setProperty("nacos.standalone", "true")
    Nacos.main(args)
}