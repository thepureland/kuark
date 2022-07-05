package io.kuark.base.bean.validation.support

import io.kuark.base.lang.reflect.getMemberProperty
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.kotlinFunction


/**
 * 参数校验切面
 *
 * @author K
 * @since 1.0.0
 */
@Aspect
@Component
@Lazy(false)
class ArgValidAspect {

    /**
     * 定义切入点
     *
     * @author K
     * @since 1.0.0
     */
    @Pointcut("@annotation(io.kuark.base.bean.validation.support.ArgValid)")
    private fun cut() {
        // do nothing
    }

    /**
     * @ArgValid标记的bean方法在调用前，应用各参数上标记的校验注解进行参数值校验
     *
     * @author K
     * @since 1.0.0
     */
    @Around("cut()")
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        val function = (joinPoint.signature as MethodSignature).method.kotlinFunction!!
        function.valueParameters.forEachIndexed { index, param ->
            val paramValue = joinPoint.args[index]
            param.annotations.forEach {annotation ->
                val validators = ValidatorFactory.getValidator(annotation, paramValue)
                validators.forEach { validator ->
                    val valid = validator.isValid(paramValue, null)
                    if (!valid) {
                        val msg = annotation::class.getMemberProperty("message").call(annotation) as String
                        throw IllegalArgumentException(msg)
                    }
                }
            }
        }
        return joinPoint.proceed()
    }

}