package org.kuark.server.context

import org.kuark.server.support.KtorMiddleware
import org.kuark.server.support.KtorRouter
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.Resource

// 此处为web框架的加载，当ktor开始启动后将陷入死循环，所以需要先让spring boot加载其他配置项
//@AutoConfigureAfter(KtormConfiguration::class)
@Configuration
@EnableConfigurationProperties(KtorConfiguration.KtorProperties::class)
open class KtorConfiguration {

    @Resource
    private lateinit var properties: KtorProperties

    /**
     * 注册ktor web容器，可自行更改
     */
    @Bean
    @ConditionalOnMissingBean
    open fun registerKtorWebContainer(): ApplicationEngineFactory<ApplicationEngine, out ApplicationEngine.Configuration> =
        Netty

    /**
     * 注册引擎
     * @param engineFactory 依赖引擎工厂
     */
    @Bean
    @ConditionalOnMissingBean
    open fun applicationEngine(
        registerKtorWebContainer: ApplicationEngineFactory<ApplicationEngine, out ApplicationEngine.Configuration>,
        context: ApplicationContext
    ): ApplicationEngine {
        return embeddedServer(registerKtorWebContainer, host = properties.host, port = properties.port) {
            val middlewares = context.getBeansOfType(KtorMiddleware::class.java).values
            val routes = context.getBeansOfType(KtorRouter::class.java).values

            // 注册模块
            middlewares.forEach {
                it.apply { middleware() }
            }

            // 注册路由
            routing {
                routes.forEach {
                    it.apply { router() }
                }
            }
        }
    }

    /**
     * 注册应用
     * @param applicationEngine 依赖引擎
     * @param context            依赖spring容器
     */
    @Bean
    @ConditionalOnMissingBean
    open fun application(applicationEngine: ApplicationEngine): Int? {
        applicationEngine.start(true)
//        Server().runTool("-web")
        return 0
    }

    /**
     * ktor配置项
     * @param host 服务器主机名
     * @param port 绑定端口
     */
    @ConfigurationProperties(prefix = "ktor")
    open class KtorProperties(
        var host: String = "127.0.0.1",
        var port: Int = 8080
    )
}