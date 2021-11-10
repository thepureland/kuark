package io.kuark.context.context

import javax.validation.ConstraintValidator
import kotlin.reflect.KClass

interface IValidatorProvider {

    fun <T: Annotation, V: ConstraintValidator<T, *>> provide(): Map<KClass<T>, KClass<V>>

}