package io.kuark.base.net.http

import org.junit.jupiter.api.Test

internal class HttpClientKitTest {

    @Test
    fun request() {
        val httpContext = HttpContext("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png")
        val singleRequest = HttpClientKit.request(httpContext)
        println(singleRequest.statusCode())
        val body = singleRequest.body()
        println(body)




    }

}