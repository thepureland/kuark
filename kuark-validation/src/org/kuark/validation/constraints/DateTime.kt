package org.kuark.validation.constraints

import org.kuark.validation.constraints.validator.DateTimeValidator
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * Create by (admin) on 2015/1/23.
 */
@Documented
@Constraint(validatedBy = [DateTimeValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
annotation class DateTime(
    val message: String = "{org.kuark.validation.constraints.DateTime.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val format: String
) {
    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
    )
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    annotation class List(vararg val value: DateTime)
}