package org.kuark.base.bean.validation.constraint.validator

import org.kuark.base.bean.validation.constraint.annotaions.CustomConstraint
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
class CustomConstraintValidator : ConstraintValidator<CustomConstraint, Any?> {

    private lateinit var customConstraint: CustomConstraint

    override fun initialize(customConstraint: CustomConstraint) {
        this.customConstraint = customConstraint
    }

    override fun isValid(value: Any?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }

        val bean = ValidationContext.get(constraintValidatorContext)
        val validatorClass = customConstraint.checkClass as KClass<IBeanValidator<Any>>
        val validator = validatorClass.java.getDeclaredConstructor().newInstance()
        return validator.validate(bean)
    }

}