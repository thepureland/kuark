package org.kuark.validation.constraints.validator

import org.kuark.validation.constraints.Qq
import org.kuark.validation.constraints.support.RegExpConstants
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Create by (admin) on 2015/1/22.
 */
class QqValidator : ConstraintValidator<Qq, String> {

    private lateinit var qq: Qq

    override fun initialize(qq: Qq) {
        this.qq = qq
    }

    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
        return value.isBlank() || value.matches(RegExpConstants.QQ.toRegex())
    }
}