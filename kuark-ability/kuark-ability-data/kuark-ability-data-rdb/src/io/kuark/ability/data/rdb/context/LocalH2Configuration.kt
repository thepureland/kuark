package io.kuark.ability.data.rdb.context

import com.zaxxer.hikari.HikariDataSource
import io.kuark.base.io.PathKit
import io.kuark.base.log.LogFactory
import io.kuark.base.net.NetworkKit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.sql.DataSource


/**
 * 本地H2数据库springboot配置类
 *
 * 如果数据库配置的是本机tcp模式的H2,将自动启动本机H2数据库及其web控制台
 *
 * @author K
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(name = ["org.h2.Driver"])
@ConditionalOnProperty(
    prefix = "spring.datasource",
    name = ["driver-class-name"],
    havingValue = "org.h2.Driver",
    matchIfMissing = false
)
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
@AutoConfigureBefore(KtormConfiguration::class)
open class LocalH2Configuration {

    private val logger = LogFactory.getLog(this::class)

    @Value("\${spring.datasource.url}")
    private lateinit var dbUrl: String

    /**
     * springboot本身就默认会以Hikari作为连接池初始化DataSource。
     * 此处的目的是解决因其他Bean自动注入DataSource时，引起的DataSource初始化早于h2数据库的启动(以tcp模式)带来的问题
     *
     * @param environment spring Environment
     * @return 数据源
     * @author K
     * @since 1.0.0
     */
    @Bean
    @Qualifier(value = "dataSource")
//    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    @ConditionalOnMissingBean
    open fun dataSource(@Autowired environment: Environment): DataSource {
        return hikariDataSource(environment)
    }

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    @ConditionalOnMissingBean
    open fun hikariDataSource(@Autowired environment: Environment): HikariDataSource {
        startH2()
        return DataSourceBuilder.create().type(HikariDataSource::class.java)
            .url(dbUrl) // Hikari直接取的连接地址参数是jdbc-url而不是url
            .build()
    }

    /**
     * 自动启动本机H2数据库及其web控制台
     *
     * @author K
     * @since 1.0.0
     */
    private fun startH2() {
        val local = dbUrl.contains("tcp") && (dbUrl.contains("localhost") || dbUrl.contains("127.0.0.1"))
        if (local) {
            resolveDbUrl()
            val tcpPort = Regex(":(\\d+)/").findAll(dbUrl).toList().flatMap(MatchResult::groupValues).last().toInt()
            val running = NetworkKit.isPortActive("localhost", tcpPort)
            if (!running) {
                org.h2.tools.Console.main("-tcp", "-web", "-pg")
                logger.debug("H2数据库及其web控制台自动启动完成")
            }
        }
    }

    /**
     * 处理配置的h2数据库url
     * 为了方便开发者，kuark实现了开箱即用，在application-ability-data-rdb-xxxx.yml文件中spring.datasource.url配置的是相对路径，
     * 这样免去开发者下载完源码运行kuark之前，需要将h2数据库所在位置改为绝对路径的麻烦。但由此带来的问题是，除非开发者在当前模块运行代码，
     * 否则，这个相对路径将是错误的，它是相对于开发者运行位置而言的。因此，这里需要动态的将其处理为绝对路径。
     *
     * @author K
     * @since 1.0.0
     */
    private fun resolveDbUrl() {
        dbUrl = dbUrl.replace(".", PathKit.getClasspath(LocalH2Configuration::class).substringBefore("/kuark-ability"))
    }

}