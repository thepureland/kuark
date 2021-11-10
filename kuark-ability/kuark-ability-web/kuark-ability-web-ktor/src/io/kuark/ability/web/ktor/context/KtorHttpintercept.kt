package io.kuark.ability.web.ktor.context

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.sessions.*
import io.kuark.ability.web.ktor.session.MixCacheSessionStorage
import io.kuark.ability.web.ktor.session.WebSession
import io.kuark.ability.web.ktor.support.KtorMiddleware
import io.kuark.context.kit.SpringKit
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class KtorHttpintercept : KtorMiddleware {

    @Value("\${web.ktor.session.id-name:sessionId}")
    private lateinit var sessionIdName: String

    @Value("\${web.ktor.session.secret-key:__Kuark__}")
    private lateinit var sessionSecretKey: String

    override fun Application.middleware() {

        var sessionStorage: SessionStorage = SpringKit.getBean(MixCacheSessionStorage::class)
//        if (sessionStorage == null) {
//            sessionStorage = directorySessionStorage(File(".sessions"))
//        }
        install(Sessions) {
            header<WebSession>(sessionIdName, sessionStorage) {
                transform(SessionTransportTransformerMessageAuthentication(sessionSecretKey.toByteArray())) // sign the ID that travels to client
            }
        }

        intercept(ApplicationCallPipeline.Features) {
            if (call.sessions.get<WebSession>() == null) {
                call.sessions.set(WebSession())
            }
        }

        // 拦截响应
        install(StatusPages) {
            // 配置500
            exception<Throwable> { err ->
                // 输出并写入到日志
//                call.error(getMessage(err))
                call.respondText("500-${err.message}", contentType = ContentType.Text.Plain)
            }

            // 配置404
            status(HttpStatusCode.NotFound) {
                call.respondText("404-未找到指定路径", contentType = ContentType.Text.Plain)
            }
        }

        install(ContentNegotiation) {
//            jackson {
//                enable(SerializationFeature.INDENT_OUTPUT)
//            }
        }
    }

    // 获取异常全部信息
    private fun getMessage(err: Throwable): String {
        val strBuffer = StringBuffer("${err.message}\n")
        for (se in err.stackTrace) {
            strBuffer.append("\tat ${se.className}(${se.fileName}:${se.lineNumber})\n")
        }
        strBuffer.deleteCharAt(strBuffer.length - 1)
        strBuffer.append("}")
        return strBuffer.toString()
    }
}