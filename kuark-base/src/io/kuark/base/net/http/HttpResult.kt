package io.kuark.base.net.http

import java.awt.Image
import java.io.FileOutputStream
import java.io.InputStream
import java.net.http.HttpResponse
import kotlin.reflect.KClass


/**
 * Created by admin 2015/10/28.
 */
class HttpResult() : IHttpResult {



    override fun <R : Any> get(resultClass: KClass<R>): R? {
        TODO("Not yet implemented")
    }

    override fun getMap(): Map<String?, Any?>? {
        TODO("Not yet implemented")
    }

    override fun getString(): String? {
        TODO("Not yet implemented")
    }

    override fun getInputStream(): ByteArray? {

//        val inputStreamBodyHandler = HttpResponse.BodyHandlers.ofInputStream()
//        val httpResponse: HttpResponse<InputStream> = client.send(request, inputStreamBodyHandler)
//        val inputStream = httpResponse.body()
//        val fis = FileOutputStream("src/com/hehui/day1214/index.html")
//        inputStream.transferTo(fis)

        TODO("Not yet implemented")
    }

    override fun getImage(): Image? {
        TODO("Not yet implemented")
    }

    override fun getResponse(): HttpResponse<*> {
        TODO("Not yet implemented")
    }

    override fun getStatusCode(): Int {
        TODO("Not yet implemented")
    }

}