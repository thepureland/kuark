package io.kuark.base.net.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

/**
 * Http Client工具类。
 * 用协程封Java11的异步非阻塞HttpClient
 *
 * @author K
 * @since 1.0.0
 */
object HttpClientKit {

//    fun request(context: HttpContext): HttpResponse<*> {
//        return runBlocking {
//            val result = async(Dispatchers.IO) {
//                fetchUrl(context)
//            }
//            result.await()
//        }
//    }
//
//    private suspend fun fetchUrl(context: HttpContext): HttpResponse<String> {
////        HttpRequest.BodyPublishers.of
//        val request = HttpRequest.newBuilder()
//            .timeout(Duration.ofMillis(context.socketTimeout))
//            .uri(URI.create(context.url))
////            .method()
////            .POST(HttpRequest.BodyPublishers.ofString(result))
//            .header("Content-Type", "application/json")
//            .build()
//        val client = createHttpClient(context)
//        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//        return response.await()
//    }
//
//    private fun createHttpClient(context: HttpContext): HttpClient =
//        HttpClient.newBuilder()
//            .version(context.version)
//            .followRedirects(context.redirect)
//            .connectTimeout(Duration.ofMillis(context.connectTimeout))
//            .build()


}