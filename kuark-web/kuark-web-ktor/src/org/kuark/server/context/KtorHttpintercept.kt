package org.kuark.server.context

import org.kuark.server.support.KtorMiddleware
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import org.springframework.stereotype.Component

@Component
class KtorHttpintercept: KtorMiddleware {
    override fun Application.middleware() {
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
    private fun getMessage(err: Throwable):String {
        val strBuffer = StringBuffer("${err.message}\n")
        for (se in err.stackTrace) {
            strBuffer.append("\tat ${se.className}(${se.fileName}:${se.lineNumber})\n")
        }
        strBuffer.deleteCharAt(strBuffer.length -1)
        strBuffer.append("}")
        return strBuffer.toString()
    }
}