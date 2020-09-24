package io.kuark.context.discovery

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Configuration

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Configuration
@ConditionalOnExpression("'\${kuark.ability.config.enabled}'.equals('true') || '\${kuark.ability.discovery.enabled}'.equals('true')")
open class DiscoveryClientConfiguration {
}