package io.kuark.ability.web.springmvc

import io.kuark.ability.web.common.WebResult
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice


/**
 * 全局响应处理，响应给前端的结果全部自动封装为WebResult
 *
 * @author K
 * @since 1.0.0
 */
@ControllerAdvice(annotations = [FrontEndApi::class])
class GlobalResponseBodyHandler : ResponseBodyAdvice<Any?> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any {
        if(body is WebResult<*>) {
            return body
        }
        return WebResult(body)
    }
}