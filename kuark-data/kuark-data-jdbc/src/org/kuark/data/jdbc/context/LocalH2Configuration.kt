package org.kuark.data.jdbc.context

import com.zaxxer.hikari.HikariDataSource
import org.kuark.base.log.LoggerFactory
import org.kuark.base.net.NetworkKit
import org.kuark.config.annotation.ConfigValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import java.nio.file.Paths
import javax.sql.DataSource


/**
 * 如果数据库配置的是本机tcp模式的H2,将自动启动本机H2数据库及其web控制台
 */
@Configuration
@ConditionalOnClass(name = ["org.h2.Driver"])
@ConditionalOnProperty(
    prefix = "spring.datasource",
    name = ["driver-class-name"],
    havingValue = "org.h2.Driver",
    matchIfMissing = false
)
@AutoConfigureBefore(KtormConfiguration::class)
open class LocalH2Configuration {

    private val logger = LoggerFactory.getLogger(this::class)

    @ConfigValue("\${spring.datasource.url}")
    private lateinit var dbUrl: String

    /**
     * springboot本身就默认会以Hikari作为连接池初始化DataSource。
     * 此处的目的是解决因其他Bean自动注入DataSource时，引起的DataSource初始化早于h2数据库的启动(以tcp模式)带来的问题
     */
    @Bean
    @Qualifier(value = "dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    @ConditionalOnMissingBean
    open fun dataSource(@Autowired environment: Environment): DataSource {
        startH2()
        return DataSourceBuilder.create().type(HikariDataSource::class.java)
            .url(dbUrl) // Hikari直接取的连接地址参数是jdbc-url而不是url
            .build()
    }

    /**
     * 自动启动本机H2数据库及其web控制台
     */
    fun startH2() {
        val local = dbUrl.contains("tcp") && (dbUrl.contains("localhost") || dbUrl.contains("127.0.0.1"))
        if (local) {
            val tcpPort = Regex(":(\\d+)/").findAll(dbUrl).toList().flatMap(MatchResult::groupValues).last().toInt()
            val running = NetworkKit.isPortActive("localhost", tcpPort)
            if (!running) {
                val path = Paths.get(".").toAbsolutePath().normalize().toString()
                val process =
                    Runtime.getRuntime().exec("cmd /E:ON /c start ${path}/kuark-data/kuark-data-jdbc/h2/h2.bat") //TODO multiple OS support
                process.waitFor()
                logger.debug("H2数据库及其web控制台自动启动完成")
            }
        }
    }

}