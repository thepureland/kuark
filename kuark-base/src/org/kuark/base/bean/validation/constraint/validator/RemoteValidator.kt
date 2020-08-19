package org.kuark.base.bean.validation.constraint.validator

import org.kuark.base.bean.validation.constraint.annotaions.Remote
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

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

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        return CustomValidator.validate(remote.checkClass, value, context)
    }

}