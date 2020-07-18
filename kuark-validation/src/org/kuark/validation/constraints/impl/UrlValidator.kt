package org.kuark.validation.constraints.impl

import org.kuark.validation.constraints.Url
import org.kuark.validation.constraints.support.RegExpConstants
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Create by (admin) on 2015/1/22.
 */
class UrlValidator : ConstraintValidator<Url, String> {

    private lateinit var url: Url

    override fun initialize(url: Url) {
        this.url = url
    }

    override fun isValid(value: String, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return value.isBlank() || value.toLowerCase().matches(RegExpConstants.URL.toRegex())
    }

}