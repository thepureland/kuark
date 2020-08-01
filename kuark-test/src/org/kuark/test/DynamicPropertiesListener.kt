package org.kuark.test

import org.kuark.base.log.LogFactory
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource

/**
 * 动态属性监听器，方便测试时按需要随时改变属性的值，以便测试不同属性值的情况
 *
 * @author K
 * @since 1.0.0
 */
class DynamicPropertiesListener(private val dynamicProperties: Map<String, String>) :
    ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private val log = LogFactory.getLog(this::class)

    override fun onApplicationEvent(event: ApplicationEnvironmentPreparedEvent) {
        val env = event.environment as ConfigurableEnvironment
        val propertySources = env.propertySources
        propertySources.addFirst(MapPropertySource("DynamicPropertySource", dynamicProperties))
        log.info("以下是被动态改变的属性：$dynamicProperties")
    }

}