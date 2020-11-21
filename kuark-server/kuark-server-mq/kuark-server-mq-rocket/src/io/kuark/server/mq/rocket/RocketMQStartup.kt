package io.kuark.server.mq.rocket

import io.kuark.base.io.PathKit
import io.kuark.base.lang.SystemKit
import org.apache.rocketmq.broker.BrokerStartup
import org.apache.rocketmq.namesrv.NamesrvStartup

/**
 * 启动RocketMQ的name server和broker
 *
 * @author K
 * @since 1.0.0
 */
fun main(vararg args: String) {
    val path = PathKit.getRuntimePath() + "../../../resources/main/"
    SystemKit.setEnvVars(mapOf("ROCKETMQ_HOME" to path))
    NamesrvStartup.main(args)
    BrokerStartup.main(args)
}