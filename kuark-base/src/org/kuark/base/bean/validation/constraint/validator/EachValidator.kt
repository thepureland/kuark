package org.kuark.base.bean.validation.constraint.validator

import org.kuark.base.bean.validation.constraint.annotaions.Each
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class EachValidator : ConstraintValidator<Each, Any?> {

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        TODO("Not yet implemented")
    }

}