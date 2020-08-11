package org.kuark.web.form.validation.js.converter.impl

import org.hibernate.validator.constraints.CreditCardNumber
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import org.kuark.web.form.validation.js.converter.AbstractConstraintConverter
import org.soul.model.common.constraints.*
import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.AbstractRuleConverter
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext
import java.util.*
import javax.validation.constraints.*

/**
 * Create by (admin) on 2015/2/16.
 */
class DefaultRuleConverter(annotation: Annotation) : AbstractConstraintConverter(annotation) {
    protected override val rulePatternMap: Map<String, Any>
        protected get() {
            val map: MutableMap<String, Any> = HashMap(2, 1f)
            val annoClass: Class<out Annotation> = annotation.annotationType()
            if (annoClass == NotNull::class.java || annoClass == NotEmpty::class.java || annoClass == NotBlank::class.java) {
                map["required"] = true
            } else if (annoClass == Email::class.java) {
                map["email"] = true
            } else if (annoClass == CreditCardNumber::class.java) {
                map["creditcard"] = true
            } else if (annoClass == Qq::class.java) {
                map["qq"] = true
            } else if (annoClass == CellPhone::class.java) {
                map["cellphone"] = true
            } else if (annoClass == Url::class.java) {
                map["url"] = true
            } else if (annoClass == BankCardNumber::class.java) {
                map["bankcard"] = true
            } else if (annoClass == DecimalMax::class.java || annoClass == Max::class.java) {
                map["max"] = "value"
            } else if (annoClass == DecimalMin::class.java || annoClass == Min::class.java) {
                map["min"] = "value"
            } else if (annoClass == Digits::class.java) {
                map["integerPrecision"] = "integer"
                map["fractionPrecision"] = "fraction"
            } else if (annoClass == Size::class.java || annoClass == Length::class.java) {
                map["minlength"] = "min"
                map["maxlength"] = "max"
            } else if (annoClass == Range::class.java) {
                map["min"] = "min"
                map["max"] = "max"
            } else if (annoClass == DateTime::class.java) {
                map["datetime"] = "format"
            } else if (annoClass == Series::class.java) {
                map["series"] = "type"
            }
            return map
        }
}