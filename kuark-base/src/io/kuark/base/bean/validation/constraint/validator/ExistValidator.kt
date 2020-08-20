package io.kuark.base.bean.validation.constraint.validator

import io.kuark.base.bean.validation.constraint.annotaions.Exist
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Exist约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class ExistValidator : ConstraintValidator<Exist, Any?> {

    private lateinit var exist: Exist

    override fun initialize(exist: Exist) {
        this.exist = exist
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }

        val constraints = exist.value
        val validator = ConstraintsValidator()
        validator.initialize(constraints)
        val pass = when (value) {
            is Array<*> -> value.any { validator.isValid(it, context) }
            is BooleanArray -> value.any { validator.isValid(it, context) }
            is ByteArray -> value.any { validator.isValid(it, context) }
            is CharArray -> value.any { validator.isValid(it, context) }
            is DoubleArray -> value.any { validator.isValid(it, context) }
            is FloatArray -> value.any { validator.isValid(it, context) }
            is IntArray -> value.any { validator.isValid(it, context) }
            is LongArray -> value.any { validator.isValid(it, context) }
            is ShortArray -> value.any { validator.isValid(it, context) }
            is Collection<*> -> value.any { validator.isValid(it, context) }
            is Map<*, *> -> value.values.any { validator.isValid(it, context) }
            else -> validator.isValid(value, context)
        }
        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(exist.message).addConstraintViolation()
        return pass
    }

}