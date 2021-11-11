package io.kuark.context.context

import javax.validation.ConstraintValidator
import kotlin.reflect.KClass

/**
 * 约束验证器提供者，关联约束注解及其验证器。子类必须为Spring Bean
 *
 * @author K
 * @since 1.0.0
 */
interface IConstraintValidatorProviderBean {

    /**
     * 提供约束注解及其要关联的验证器
     *
     * @return Map(约束类，验证器类)
     * @author K
     * @since 1.0.0
     */
    fun <T: Annotation, V: ConstraintValidator<T, *>> provide(): Map<KClass<T>, KClass<V>>

}