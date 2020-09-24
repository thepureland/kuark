package io.kuark.context.discovery

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Configuration

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Configuration
@ConditionalOnExpression("'\${kuark.ability.config.enabled}'.equals('true') || '\${kuark.ability.discovery.enabled}'.equals('true')")
@EnableDiscoveryClient
open class DiscoveryClientConfiguration {
}