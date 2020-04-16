package org.kuark.config.context

import com.alibaba.nacos.api.annotation.NacosProperties
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource
import org.springframework.context.annotation.Configuration

@Configuration
//@EnableNacosConfig(globalProperties = NacosProperties(serverAddr = "127.0.0.1:8844"))
@EnableNacosConfig
@NacosPropertySource(dataId = "springboot2-nacos-config", autoRefreshed = true)
open class NacosConfig