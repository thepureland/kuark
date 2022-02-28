package io.kuark.ability.web.springmvc

import io.kuark.ability.web.common.WebResult
import io.kuark.base.log.LogFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


/**
 * 非法参数请求异常处理。当参数校验(@Valid)不通过时，返回统一提示。
 * 注：生效条件为请求的方法的参数未出现Errors类的实例时（BindingResult为Errors的子接口）
 *
 * @author K
 * @since 1.0.0
 */
@ControllerAdvice
class BadRequestExceptionHandler: ResponseEntityExceptionHandler() {

    private val log = LogFactory.getLog(this::class)

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errMsg = "非法请求参数！"
        log.error(ex, errMsg)
        val result = WebResult<Any?>(errMsg, 500)
        return ResponseEntity<Any>(result, headers, status)
    }

}