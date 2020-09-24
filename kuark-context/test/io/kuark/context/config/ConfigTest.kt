package io.kuark.context.config

import io.kuark.test.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
//@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
@Component
class ConfigTest: SpringTest() {

    @Value("\${config.info}")
    private val configInfo: String? = null

    /**
     * 从配置中心获取属性，需要启动注册中心和配置中心
     */
    @Test
    fun getPropertyFromConfigCenter() {
        println(configInfo)
    }

    /**
     * 从本地配置文件获取属性，不用启动注册中心和配置中心
     */
    @Test
    fun getPropertyLocal() {

    }

}