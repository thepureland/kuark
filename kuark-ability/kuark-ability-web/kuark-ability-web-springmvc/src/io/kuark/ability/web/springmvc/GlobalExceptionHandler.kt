package io.kuark.ability.web.springmvc

import io.kuark.ability.web.common.WebResult
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Web全局异常处理
 *
 * @author K
 * @since 1.0.0
 */
@ControllerAdvice
open class GlobalExceptionHandler {

    /** 未知错误的提示消息 */
    protected var unknownErrorMsg = "服务端发生未知错误！"

    /** 是否要打印异常日志 */
    protected var shouldLog = true

    protected val log = LogFactory.getLog(this::class)

    @ExceptionHandler
    @ResponseBody
    open fun handle(requset: HttpServletRequest, response: HttpServletResponse, e: Throwable): WebResult<*> {
        val msg = if (StringKit.isBlank(e.message)) {
            unknownErrorMsg
        } else {
            e.message
        }
        if (shouldLog) {
            log.error(e)
        }
        return WebResult<Any?>(msg, 500)
    }

}