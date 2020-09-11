package io.kuark.base.net.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublisher
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets


/**
 * Http Client工具类。
 * 用协程封装Java11的异步非阻塞HttpClient
 *
 * @author K
 * @since 1.0.0
 */
object HttpClientKit {

    /**
     * 使用协程发起异步请求(不会阻塞线程)，并等待结果返回
     */
    fun <T : Any> asyncRequest(
        httpClientBuilder: HttpClient.Builder,
        httpRequestBuilder: HttpRequest.Builder,
        bodyHandler: HttpResponse.BodyHandler<T>
    ): HttpResponse<T> {
        return runBlocking {
            val result = async(Dispatchers.IO) {
                sendAsyncThenWait(httpClientBuilder, httpRequestBuilder, bodyHandler)
            }
            result.await()
        }
    }

    private suspend fun <T : Any> sendAsyncThenWait(
        httpClientBuilder: HttpClient.Builder,
        httpRequestBuilder: HttpRequest.Builder,
        bodyHandler: HttpResponse.BodyHandler<T>
    ): HttpResponse<T> {
        val request = httpRequestBuilder.build()
        val client = createHttpClient(httpClientBuilder)
        val response = client.sendAsync(request, bodyHandler)
        return response.await()
    }

    private fun createHttpClient(httpClientBuilder: HttpClient.Builder): HttpClient = httpClientBuilder.build()

    fun ofFormData(data: Map<Any, Any>): BodyPublisher {
        val builder = StringBuilder()
        data.forEach { (key, value) ->
            if (builder.isNotEmpty()) {
                builder.append("&")
            }
            builder.append(URLEncoder.encode(key.toString(), StandardCharsets.UTF_8))
            builder.append("=")
            builder.append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8))
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString())
    }

}