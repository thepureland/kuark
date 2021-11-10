package io.kuark.service.sys.provider.validation

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.context.context.IValidatorProvider
import org.springframework.stereotype.Component
import javax.validation.ConstraintValidator
import kotlin.reflect.KClass

@Component
class DictValidationProvider: IValidatorProvider {

    override fun <T : Annotation, V : ConstraintValidator<T, *>> provide(): Map<KClass<T>, KClass<V>> {
        return mapOf(DictCode::class to DictCodeValidator::class) as Map<KClass<T>, KClass<V>>
    }

}