package org.kuark.test

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
//@Component
class LocalEnvironmentPrepareEventListener: ApplicationListener<ApplicationEnvironmentPreparedEvent> {

//    @EventListener
    override fun onApplicationEvent(event: ApplicationEnvironmentPreparedEvent) {
        val env = event.environment as ConfigurableEnvironment
        val propertySources = env.propertySources
        val map = mapOf("cache.config.strategy" to "LOCAL_REMOTE")
        propertySources.addFirst(MapPropertySource("DynamicPropertySource", map))

//        propertySources.filter { it.name.contains("applicationConfig") }.forEach {
//            val source = it.source as Map<String, OriginTrackedValue>
//            val originTrackedValue = source["cache.config.strategy"]
//            source["cache.config.strategy"] = OriginTrackedValue.of("LOCAL_REMOTE", originTrackedValue!!.origin)
//
//        }
    }

}