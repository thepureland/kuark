package io.kuark.ability.distributed.lock.context

import io.kuark.ability.distributed.lock.core.RedissonLock
import io.kuark.ability.distributed.lock.core.IRedissonLock
import io.kuark.base.lang.string.StringKit
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.BaseConfig
import org.redisson.config.Config
import org.redisson.config.ReadMode
import org.redisson.config.TransportMode
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@ConditionalOnProperty(prefix = "redisson", name = ["enabled"], havingValue = "true", matchIfMissing = false)
open class DistributedLockConfiguration {

    @Bean
    open fun redissonProperties(): RedissonProperties {
        return RedissonProperties()
    }

    /**
     * 单机模式自动装配
     * @return
     */
    @Bean
    open fun redissonSingle(properties: RedissonProperties): RedissonClient { // 这里不用Config.fromYAML()进行构造Config的原因是：统一使配置中心的使用方式
        val config = Config().apply {
            threads = properties.threads
            nettyThreads = properties.nettyThreads
//            codec = redissonProperties.codec //TODO
            transportMode = TransportMode.valueOf(properties.transportMode)
        }

        when (properties.mode) {
            "single" -> config.useSingleServer().apply {
                subscriptionConnectionMinimumIdleSize = properties.subscriptionConnectionMinimumIdleSize
                subscriptionConnectionPoolSize = properties.subscriptionConnectionPoolSize
                dnsMonitoringInterval = properties.dnsMonitoringInterval
                initBaseConfig(this, properties)
                address = properties.address
                connectionMinimumIdleSize = properties.connectionMinimumIdleSize
                connectionPoolSize = properties.connectionPoolSize
                database = properties.database
            }
            "cluster" -> config.useClusterServers().apply {
                subscriptionConnectionMinimumIdleSize = properties.subscriptionConnectionMinimumIdleSize
                subscriptionConnectionPoolSize = properties.subscriptionConnectionPoolSize
                dnsMonitoringInterval = properties.dnsMonitoringInterval
                initBaseConfig(this, properties)
//            loadBalancer = properties.loadBalancer //TODO
                slaveConnectionMinimumIdleSize = properties.slaveConnectionMinimumIdleSize
                slaveConnectionPoolSize = properties.slaveConnectionPoolSize
                masterConnectionMinimumIdleSize = properties.masterConnectionMinimumIdleSize
                masterConnectionPoolSize = properties.masterConnectionPoolSize
                readMode = ReadMode.valueOf(properties.readMode)
                scanInterval = properties.scanInterval
                addNodeAddress(*properties.nodeAddresses)
            }
        }

        return Redisson.create(config)
    }

    private fun initBaseConfig(baseConfig: BaseConfig<*>, properties: RedissonProperties) {
        with(baseConfig) {
            pingConnectionInterval = properties.pingConnectionInterval
            idleConnectionTimeout = properties.idleConnectionTimeout
            connectTimeout = properties.connectTimeout
            timeout = properties.timeout
            retryAttempts = properties.retryAttempts
            retryInterval = properties.retryInterval
            if (StringKit.isNotBlank(properties.password)) {
                password = properties.password
            }
            subscriptionsPerConnection = properties.subscriptionsPerConnection
            clientName = properties.clientName

        }
    }

    @Bean
    open fun distributedLock(redissonSingle: RedissonClient): IRedissonLock {
        return RedissonLock(redissonSingle)
    }

}