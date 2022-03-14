package io.kuark.service.sys.common.validation

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.base.support.Consts
import io.kuark.context.context.IConstraintValidatorProviderBean
import org.springframework.stereotype.Component
import javax.validation.ConstraintValidator
import kotlin.reflect.KClass

/**
 * DictCode约束验证器提供者
 *
 * @author K
 * @since 1.0.0
 */
@Component
class DictCodeConstraintValidatorProvider: IConstraintValidatorProviderBean {

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun <T : Annotation, V : ConstraintValidator<T, *>> provide(): Map<KClass<T>, KClass<V>> {
        return mapOf(DictCode::class to DictCodeValidator::class) as Map<KClass<T>, KClass<V>>
    }

}