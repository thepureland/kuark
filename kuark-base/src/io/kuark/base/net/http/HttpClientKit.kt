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
import java.time.Duration


/**
 * Http Client工具类。
 *
 * 封装Java11的HttpClient，其具有以下特点：支持异步，支持reactive streams，同时也支持了HTTP2以及WebSocket
 *
 * ???两次调用报：java.io.IOException: 远程主机强迫关闭了一个现有的连接。
 *
 * @author K
 * @since 1.0.0
 */
object HttpClientKit {

    /**
     * 使用协程发起异步请求(不会阻塞线程)，并等待结果返回
     *
     * @param T 返回的response body类型
     * @param httpClientBuilder HttpClient.Builder对象，通过HttpClient.newBuilder()创建，并链式调用各配置方法
     * @param httpRequestBuilder HttpRequest.Builder对象，通过HttpRequest.newBuilder()创建，并链式调用各配置方法
     * @param bodyHandler HttpResponse.BodyHandler对象，通过HttpResponse.BodyHandlers.ofXXXX方法创建
     * @return 响应结果HttpResponse对象
     * @author K
     * @since 1.0.0
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

    fun <T : Any> request(
        httpClientBuilder: HttpClient.Builder,
        httpRequestBuilder: HttpRequest.Builder,
        bodyHandler: HttpResponse.BodyHandler<T>
    ): HttpResponse<T> {
        val client = createHttpClient(httpClientBuilder)
        val request = httpRequestBuilder.build()
        return client.send(request, bodyHandler)
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

    private fun createHttpRequest(httpRequestBuilder: HttpRequest.Builder): HttpRequest {
        return httpRequestBuilder.build()
    }

    private fun createHttpClient(httpClientBuilder: HttpClient.Builder): HttpClient {
        return httpClientBuilder.build()
    }

    fun ofFormData(data: Map<Any, Any>): BodyPublisher {
//        .header("Content-Type","application/x-www-form-urlencoded")
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