package io.kuark.ability.web.ktor.context

import io.ktor.routing.*
import io.ktor.server.engine.*
import io.kuark.ability.web.ktor.support.KtorMiddleware
import io.kuark.ability.web.ktor.support.KtorRouter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

// 此处为web框架的加载，当ktor开始启动后将陷入死循环，所以需要先让spring boot加载其他配置项
@Configuration
open class KtorConfiguration {

    @Value("\${web.ktor.container.type:NETTY}")
    private lateinit var webContainer: String

    @Value("\${web.ktor.container.host:127.0.0.1}")
    private lateinit var webContainerHost: String

    @Value("\${web.ktor.container.port:8080}")
    private lateinit var webContainerPort: String

    /**
     * 注册ktor web容器
     */
    @Bean
    open fun registerKtorWebContainer(): ApplicationEngineFactory<ApplicationEngine, out ApplicationEngine.Configuration> {
        val clazzStr = when(webContainer.uppercase()) {
            "NETTY" -> "io.ktor.server.netty.Netty"
            "JETTY" -> "io.ktor.server.jetty.Jetty"
            "TOMCAT" -> "io.ktor.server.tomcat.Tomcat"
            "CIO" -> "io.ktor.server.cio.CIO"
            else -> error("不支持的web容器类型：$webContainer")
        }
        val clazz = try {
             Class.forName(clazzStr)
        } catch (e: ClassNotFoundException) {
            error("类${clazzStr}找不到，请确保依赖存在：io.ktor:ktor-server-${webContainer.lowercase()}")
        }
        val instance = clazz.getDeclaredField("INSTANCE")
        return instance.get(null) as ApplicationEngineFactory<ApplicationEngine, out ApplicationEngine.Configuration>
    }


    /**
     * 注册引擎
     * @param engineFactory 依赖引擎工厂
     */
    @Bean
    open fun applicationEngine(
        registerKtorWebContainer: ApplicationEngineFactory<ApplicationEngine, out ApplicationEngine.Configuration>,
        context: ApplicationContext
    ): ApplicationEngine {
        return embeddedServer(registerKtorWebContainer, host = webContainerHost, port = webContainerPort.toInt()) {
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
     */
    @Bean
    open fun application(applicationEngine: ApplicationEngine): Int? {
        applicationEngine.start(true)
//        Server().runTool("-web")
        return 0
    }

}