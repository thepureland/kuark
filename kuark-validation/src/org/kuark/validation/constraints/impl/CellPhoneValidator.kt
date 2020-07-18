package org.kuark.validation.constraints.impl

import org.kuark.validation.constraints.CellPhone
import org.kuark.validation.constraints.support.RegExpConstants
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Create by (admin) on 2015/1/22.
 */
class CellPhoneValidator : ConstraintValidator<CellPhone, String> {

    private lateinit var cellPhone: CellPhone

    override fun initialize(cellPhone: CellPhone) {
        this.cellPhone = cellPhone
    }

    override fun isValid(value: String, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return value.isBlank() || value.matches(RegExpConstants.CELL_PHONE.toRegex())
    }

}