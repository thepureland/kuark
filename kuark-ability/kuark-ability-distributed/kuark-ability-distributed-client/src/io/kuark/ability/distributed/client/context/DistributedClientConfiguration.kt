package io.kuark.ability.distributed.client.context

import org.springframework.beans.factory.annotation.Configurable
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients


@Configurable
@EnableFeignClients
@EnableDiscoveryClient
class DistributedClientConfiguration {

}