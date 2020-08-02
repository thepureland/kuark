package org.kuark.base.validation.constraint.annotaions

import org.kuark.base.validation.support.SeriesType
import org.kuark.base.validation.constraint.validator.SeriesValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 数列表单验证规则
 *
 * @author admin
 * @time 9/19/15 6:50 PM
 */
@MustBeDocumented
@Constraint(validatedBy = [SeriesValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
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