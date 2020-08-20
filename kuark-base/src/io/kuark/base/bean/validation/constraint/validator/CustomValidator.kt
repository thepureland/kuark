package io.kuark.base.bean.validation.constraint.validator

import io.kuark.base.bean.validation.constraint.annotaions.Custom
import io.kuark.base.bean.validation.support.IBeanValidator
import io.kuark.base.bean.validation.support.ValidationContext
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

/**
 * Custom约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class CustomValidator : ConstraintValidator<Custom, Any?> {

    private lateinit var custom: Custom

    override fun initialize(custom: Custom) {
        this.custom = custom
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        return validate(custom.checkClass, value, context)
    }

    companion object {
        fun validate(
            checkClass: KClass<out IBeanValidator<*>>, value: Any?, context: ConstraintValidatorContext
        ): Boolean {
            if (value == null) {
                return true
            }

            val bean = ValidationContext.get(context)!!
            val validatorClass = checkClass as KClass<IBeanValidator<Any>>
            val validator = validatorClass.java.getDeclaredConstructor().newInstance()
            return validator.validate(bean)
        }
    }

}