package io.kuark.base.bean.validation.constraint.validator

import io.kuark.base.bean.validation.constraint.annotaions.DateTime
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * DateTime约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class DateTimeValidator : ConstraintValidator<DateTime, String?> {

    private lateinit var dateTime: DateTime

    override fun initialize(dateTime: DateTime) {
        this.dateTime = dateTime
    }

    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
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