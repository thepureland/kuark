package io.kuark.context.config

import com.alibaba.nacos.api.config.annotation.NacosValue
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
@Component
@EnableNacosConfig
@NacosPropertySource(dataId = "springboot2-nacos-config2.yml", autoRefreshed = true)
@EnableDiscoveryClient
open class ConfigClientTest: SpringTest() {

    @NacosValue("\${config.info:DEFAULT_VALUE}")
    private val configRemote: String? = null

    @Value("\${config.info:DEFAULT_VALUE}")
    private val configRemote2: String? = null

    @Value("\${spring.cloud.config.uri:DEFAULT_VALUE}")
    private val configLocal: String? = null

    @Value("\${configNoExists:DEFAULT_VALUE}")
    private val configNoExists: String? = null

    /**
     * 从配置中心获取属性，需要启动注册中心和配置中心
     */
    @Test
    fun getConfigRemote() {
        println("################################  configRemote:  $configRemote")
        println("################################  configRemote2:  $configRemote2")
    }

    /**
     * 从本地配置文件获取属性，不用启动注册中心和配置中心
     */
    @Test
    fun getConfigLocal() {
        println("configLocal:  $configLocal")
    }

    /**
     * 不存在的配置，取默认值
     */
    @Test
    fun getConfigNoExists() {
        println("configNoExists:  $configNoExists")
    }

}