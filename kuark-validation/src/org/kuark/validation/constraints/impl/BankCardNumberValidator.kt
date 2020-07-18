package org.kuark.validation.constraints.impl

import org.kuark.validation.constraints.BankCardNumber
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Create by (admin) on 2015/2/15.
 */
class BankCardNumberValidator : ConstraintValidator<BankCardNumber, String?> {

    //	private LuhnValidator luhnValidator;
    //	public BankCardNumberValidator() {
    //		luhnValidator = new LuhnValidator(1);
    //	}

    override fun initialize(bankCardNumber: BankCardNumber) {}

    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
//		if (value == null) {
//			return true;
//		}
//		return luhnValidator.passesLuhnTest(value);
        return true
    }

}