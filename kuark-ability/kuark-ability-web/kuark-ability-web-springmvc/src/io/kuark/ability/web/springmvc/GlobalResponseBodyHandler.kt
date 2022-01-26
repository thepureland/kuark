package io.kuark.ability.web.springmvc

import io.kuark.ability.web.common.WebResult
import io.kuark.base.data.json.JsonKit
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
@ControllerAdvice
class GlobalResponseBodyHandler : ResponseBodyAdvice<Any?> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return returnType.containingClass.packageName.contains("frontend")
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
        } else if(body is String) { // 因为handler处理类的返回类型是String，为了保证一致性，这里需要将ResponseResult转回去
            // https://blog.csdn.net/weixin_33961829/article/details/92143322
            return JsonKit.toJson(WebResult(body))
        }
        return WebResult(body)
    }

}