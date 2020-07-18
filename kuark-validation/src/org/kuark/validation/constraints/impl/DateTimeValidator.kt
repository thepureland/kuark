package org.kuark.validation.constraints.impl

import org.kuark.validation.constraints.DateTime
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Create by (admin) on 2015/1/23.
 */
class DateTimeValidator : ConstraintValidator<DateTime, String> {

    private lateinit var dateTime: DateTime

    override fun initialize(dateTime: DateTime) {
        this.dateTime = dateTime
    }

    override fun isValid(value: String, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (value.isBlank()) return true
        val format: String = dateTime.format
        var valid = format.length == value.length
        if (valid) {
            try {
                SimpleDateFormat(format).parse(value)
            } catch (e: ParseException) {
                valid = false
            }
        }
        return valid
    }

}