package io.kuark.ability.distributed.lock.context

import org.springframework.beans.factory.annotation.Value

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
class RedissonProperties {

    //region 自定义的属性

    @Value("\${redisson.enabled:false}")
    val enabled: Boolean = false

    @Value("\${redisson.mode:single}")
    val mode: String = "single"

    //endregion 自定义的属性


    //region Config的属性

    @Value("\${redisson.config.threads:0}")
    val threads: Int = 0

    @Value("\${redisson.config.nettyThreads:0}")
    val nettyThreads: Int = 0

    @Value("\${redisson.config.codec:!<org.redisson.codec.JsonJacksonCodec> {}}")
    val codec: String = "!<org.redisson.codec.JsonJacksonCodec> {}"

    @Value("\${redisson.config.transportMode:NIO}")
    val transportMode: String = "NIO"

    //endregion Config的属性


    //region SingleServerConfig 和 ClusterServersConfig均有的属性

    @Value("\${redisson.baseConfig.pingConnectionInterval:0}")
    val pingConnectionInterval: Int = 0

    @Value("\${redisson.baseConfig.idleConnectionTimeout:10000}")
    val idleConnectionTimeout: Int = 10000

    @Value("\${redisson.baseConfig.connectTimeout:10000}")
    val connectTimeout: Int = 10000

    @Value("\${redisson.baseConfig.timeout:10000}")
    val timeout: Int = 10000

    @Value("\${redisson.baseConfig.retryAttempts:3}")
    val retryAttempts: Int = 10000

    @Value("\${redisson.baseConfig.retryInterval:1500}")
    val retryInterval: Int = 1500

    @Value("\${redisson.baseConfig.password:}")
    val password: String = ""

    @Value("\${redisson.baseConfig.subscriptionsPerConnection:5}")
    val subscriptionsPerConnection: Int = 5

    @Value("\${redisson.baseConfig.clientName:null}")
    val clientName: String = ""

    @Value("\${redisson.baseConfig.subscriptionConnectionMinimumIdleSize:1}")
    val subscriptionConnectionMinimumIdleSize: Int = 1

    @Value("\${redisson.baseConfig.subscriptionConnectionPoolSize:50}")
    val subscriptionConnectionPoolSize: Int = 50

    @Value("\${redisson.baseConfig.dnsMonitoringInterval:5000}")
    val dnsMonitoringInterval: Long = 5000

    //endregion SingleServerConfig 和 ClusterServersConfig均有的属性


    //region SingleServerConfig的属性

    @Value("\${redisson.singleServerConfig.address:}")
    val address: String = ""

    @Value("\${redisson.singleServerConfig.connectionMinimumIdleSize:32}")
    val connectionMinimumIdleSize: Int = 32

    @Value("\${redisson.singleServerConfig.connectionPoolSize:64}")
    val connectionPoolSize: Int = 64

    @Value("\${redisson.singleServerConfig.database:0}")
    val database: Int = 0

    //endregion SingleServerConfig的属性


    //region ClusterServersConfig的属性

    @Value("\${redisson.clusterServersConfig.loadBalancer:!<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}}")
    val loadBalancer: String = "!<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}"

    @Value("\${redisson.clusterServersConfig.slaveConnectionMinimumIdleSize:32}")
    val slaveConnectionMinimumIdleSize: Int = 32

    @Value("\${redisson.clusterServersConfig.slaveConnectionPoolSize:64}")
    val slaveConnectionPoolSize: Int = 64

    @Value("\${redisson.clusterServersConfig.masterConnectionMinimumIdleSize:32}")
    val masterConnectionMinimumIdleSize: Int = 32

    @Value("\${redisson.clusterServersConfig.masterConnectionPoolSize:32}")
    val masterConnectionPoolSize: Int = 64

    @Value("\${redisson.clusterServersConfig.readMode:SLAVE}")
    val readMode: String = "SLAVE"

    @Value("\${redisson.clusterServersConfig.scanInterval:1000}")
    val scanInterval: Int = 1000

    @Value("\${redisson.clusterServersConfig.nodeAddresses:}")
    val nodeAddresses: Array<String> = arrayOf("")

    //endregion ClusterServersConfig的属性

}