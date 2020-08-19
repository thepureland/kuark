package org.kuark.base.bean.validation.constraint.validator

import org.kuark.base.bean.validation.constraint.annotaions.Each
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Each约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class EachValidator : ConstraintValidator<Each, Any?> {

    private lateinit var each: Each

    override fun initialize(each: Each) {
        this.each = each
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }

        val constraints = each.value
        val validator = ConstraintsValidator()
        validator.initialize(constraints)
        return when (value) {
            is Array<*> -> value.all { validator.isValid(it, context) }
            is BooleanArray -> value.all { validator.isValid(it, context) }
            is ByteArray -> value.all { validator.isValid(it, context) }
            is CharArray -> value.all { validator.isValid(it, context) }
            is DoubleArray -> value.all { validator.isValid(it, context) }
            is FloatArray -> value.all { validator.isValid(it, context) }
            is IntArray -> value.all { validator.isValid(it, context) }
            is LongArray -> value.all { validator.isValid(it, context) }
            is ShortArray -> value.all { validator.isValid(it, context) }
            is Collection<*> -> value.all { validator.isValid(it, context) }
            is Map<*, *> -> value.values.all { validator.isValid(it, context) }
            else -> validator.isValid(value, context)
        }
    }

}