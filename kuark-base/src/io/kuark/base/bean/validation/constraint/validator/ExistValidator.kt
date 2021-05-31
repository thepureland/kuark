package io.kuark.base.bean.validation.constraint.validator

import io.kuark.base.bean.validation.constraint.annotaions.Exist
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl
import org.hibernate.validator.internal.engine.path.PathImpl
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

        val propName = (context as ConstraintValidatorContextImpl).constraintViolationCreationContexts.first().path.leafNode.name
        // 新建临时context对象的目的是为了避免context会有子约束的错误信息，子约束的message无意义，最终的错误信息是取主约束Exist的message
        val tempContext = ConstraintValidatorContextImpl(context.clockProvider, PathImpl.createPathFromString(propName),
            context.constraintDescriptor, null)

        val pass = when (value) {
            is Array<*> -> value.any { validator.isValid(it, tempContext) }
            is BooleanArray -> value.any { validator.isValid(it, tempContext) }
            is ByteArray -> value.any { validator.isValid(it, tempContext) }
            is CharArray -> value.any { validator.isValid(it, tempContext) }
            is DoubleArray -> value.any { validator.isValid(it, tempContext) }
            is FloatArray -> value.any { validator.isValid(it, tempContext) }
            is IntArray -> value.any { validator.isValid(it, tempContext) }
            is LongArray -> value.any { validator.isValid(it, tempContext) }
            is ShortArray -> value.any { validator.isValid(it, tempContext) }
            is Collection<*> -> value.any { validator.isValid(it, tempContext) }
            is Map<*, *> -> value.values.any { validator.isValid(it, tempContext) }
            else -> validator.isValid(value, tempContext)
        }

        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(exist.message).addConstraintViolation()
        return pass
    }

}