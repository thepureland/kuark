package io.kuark.ability.data.redis.context

import io.lettuce.core.ClientOptions
import io.lettuce.core.ReadFrom
import io.lettuce.core.SocketOptions
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import java.time.Duration

/**
 * redis集群的spring boot配置类
 * 解决以下问题：
 *   Redis集群中某个master节点不稳定连接不上，导致SpringBoot应用连接Redis报错，报错连接timeout.
 *   解决方案是设置“取消校验集群节点的成员关系”，详情见：https://www.cnblogs.com/gavincoder/p/12731833.html
 *
 * @since 1.0.0
 */
//@Data
@Configuration
@ConditionalOnProperty(name = ["redis.cluster.enabled"], havingValue = "true")
open class RedisClusterConfiguration {

    @Autowired
    private lateinit var redisProperties: RedisProperties

    // 在构建LettuceConnectionFactory时，如果不使用内置的destroyMethod，可能会导致Redis连接早于其它Bean被销毁
    @Bean(destroyMethod = "destroy")
    open fun newLettuceConnectionFactory(): LettuceConnectionFactory {
        // 配置用于开启自适应刷新和定时刷新。如自适应刷新不开启，Redis集群变更时将会导致连接异常
        val clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
            .enablePeriodicRefresh(Duration.ofSeconds(60)) // 开启周期刷新(默认60秒)
            .enableAdaptiveRefreshTrigger(
                ClusterTopologyRefreshOptions.RefreshTrigger.ASK_REDIRECT,
                ClusterTopologyRefreshOptions.RefreshTrigger.UNKNOWN_NODE
            ) // 开启自适应刷新
            .build()
        val clusterClientOptions = ClusterClientOptions.builder()
            .topologyRefreshOptions(clusterTopologyRefreshOptions) //拓扑刷新
            .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
            .autoReconnect(true)
            .socketOptions(SocketOptions.builder().keepAlive(true).build())
            .validateClusterNodeMembership(false) // 取消校验集群节点的成员关系
            .build()
        val clientConfig = LettuceClientConfiguration.builder()
            .clientOptions(clusterClientOptions)
            .readFrom(ReadFrom.REPLICA_PREFERRED)
            .build()
        return LettuceConnectionFactory(clusterConfiguration, clientConfig)
    }

    private val clusterConfiguration: org.springframework.data.redis.connection.RedisClusterConfiguration
        get() {
            val clusterProperties = redisProperties.cluster
            val config = org.springframework.data.redis.connection.RedisClusterConfiguration(clusterProperties.nodes)
            if (clusterProperties.maxRedirects != null) {
                config.maxRedirects = clusterProperties.maxRedirects
            }
            if (redisProperties.password != null) {
                config.password = RedisPassword.of(redisProperties.password)
            }
            return config
        }

}