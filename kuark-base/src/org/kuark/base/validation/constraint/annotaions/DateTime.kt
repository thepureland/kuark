package org.kuark.base.validation.constraint.annotaions

import org.kuark.base.validation.constraint.validator.DateTimeValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * Create by (admin) on 2015/1/23.
 */
@MustBeDocumented
@Constraint(validatedBy = [DateTimeValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class DateTime(
    val message: String = "{DateTime.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val format: String
) {
    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
    )
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class List(vararg val value: DateTime)
}