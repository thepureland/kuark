package org.kuark.base.bean.validation.constraint.validator

import org.kuark.base.bean.validation.constraint.annotaions.Remote
import org.kuark.base.bean.validation.support.IBeanValidator
import org.kuark.base.bean.validation.support.ValidationContext
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

/**
 * Remote约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class RemoteValidator : ConstraintValidator<Remote, Any?> {

    private lateinit var remote: Remote

    override fun initialize(remote: Remote) {
        this.remote = remote
    }

    override fun isValid(value: Any?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }

        val bean = ValidationContext.get(constraintValidatorContext)
        val validatorClass = remote.checkClass as KClass<IBeanValidator<Any>>
        val validator = validatorClass.java.getDeclaredConstructor().newInstance()
        return validator.validate(bean)
    }

}