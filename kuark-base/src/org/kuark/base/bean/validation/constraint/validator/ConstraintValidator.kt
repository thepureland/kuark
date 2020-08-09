package org.kuark.base.bean.validation.constraint.validator

import org.kuark.base.bean.validation.constraint.annotaions.Constraint
import org.kuark.base.bean.validation.support.IBeanValidator
import org.kuark.base.bean.validation.support.ValidationContext
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

/**
 * Constraint约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class ConstraintValidator : ConstraintValidator<Constraint, Any?> {

    private lateinit var constraint: Constraint

    override fun initialize(constraint: Constraint) {
        this.constraint = constraint
    }

    override fun isValid(value: Any?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }

        val bean = ValidationContext.get(constraintValidatorContext)
        val validatorClass = constraint.checkClass as KClass<IBeanValidator<Any>>
        val validator = validatorClass.java.getDeclaredConstructor().newInstance()
        return validator.validate(bean)
    }

}