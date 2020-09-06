package io.kuark.base.net.http

import java.awt.Image
import java.net.http.HttpResponse
import kotlin.reflect.KClass

/**
 * Create by (admin) on 15-12-22.
 */
interface IHttpResult {

    operator fun <R : Any> get(resultClass: KClass<R>): R?

    fun getMap(): Map<String?, Any?>?

    fun getString(): String?

    fun getInputStream(): ByteArray?

    fun getImage(): Image?

    fun getResponse(): HttpResponse<*>

    fun getStatusCode(): Int

}