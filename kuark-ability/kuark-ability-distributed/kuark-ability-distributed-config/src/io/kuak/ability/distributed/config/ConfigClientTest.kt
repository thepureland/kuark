package io.kuak.ability.distributed.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
@RestController
open class ConfigClientTest {

    @Value("\${config.info}")
    private val configInfo: String? = null

    @GetMapping("/configInfo")
    fun getConfigInfo(): String? {
        return configInfo
    }

}