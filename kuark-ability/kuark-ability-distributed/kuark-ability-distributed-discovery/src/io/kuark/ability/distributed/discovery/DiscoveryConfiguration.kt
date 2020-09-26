package io.kuark.ability.distributed.discovery

import org.springframework.beans.factory.annotation.Configurable
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Configurable
@ConditionalOnProperty(
    prefix = "kuark.ability.discovery",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = false
)
@EnableDiscoveryClient
//@EnableEurekaClient
class DiscoveryConfiguration {
}