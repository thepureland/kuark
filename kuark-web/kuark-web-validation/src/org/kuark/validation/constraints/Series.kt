package org.kuark.validation.constraints

import org.kuark.validation.constraints.validator.SeriesValidator
import org.kuark.validation.constraints.support.SeriesType
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 数列表单验证规则
 *
 * @author admin
 * @time 9/19/15 6:50 PM
 */
@Documented
@Constraint(validatedBy = [SeriesValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
annotation class Series(
    val message: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    /**
     * 数列类型，默认为递增
     *
     * @return SeriesType枚举元素
     */
    val type: SeriesType = SeriesType.INC
) 