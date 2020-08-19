package org.kuark.base.bean.validation.constraint.validator

import org.kuark.base.bean.validation.constraint.annotaions.Custom
import org.kuark.base.bean.validation.support.IBeanValidator
import org.kuark.base.bean.validation.support.ValidationContext
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

/**
 * CustomConstraint约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class CustomConstraintValidator : ConstraintValidator<Custom, Any?> {

    private lateinit var custom: Custom

    override fun initialize(custom: Custom) {
        this.custom = custom
    }

    override fun isValid(value: Any?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }

        val bean = ValidationContext.get(constraintValidatorContext)!!
        val validatorClass = custom.checkClass as KClass<IBeanValidator<Any>>
        val validator = validatorClass.java.getDeclaredConstructor().newInstance()
        return validator.validate(bean)
    }

}