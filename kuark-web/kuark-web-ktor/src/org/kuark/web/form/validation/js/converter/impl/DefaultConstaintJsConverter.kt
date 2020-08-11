package org.kuark.web.form.validation.js.converter.impl

import org.hibernate.validator.constraints.CreditCardNumber
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import org.kuark.base.bean.validation.constraint.annotaions.DateTime
import org.kuark.base.bean.validation.constraint.annotaions.Series
import org.kuark.web.form.validation.js.converter.AbstractConstraintConverter
import javax.validation.constraints.*

/**
 * 默认的约束注解->js校验规则转换器
 *
 * @author K
 * @since 1.0.0
 */
class DefaultConstaintJsConverter(annotation: Annotation) : AbstractConstraintConverter(annotation) {

    override val rulePatternMap: Map<String, Any>
        get() =
            when (annotation.annotationClass) {
                NotNull::class, NotEmpty::class, NotBlank::class -> mapOf("required" to true)
                Email::class -> mapOf("email" to true)
                CreditCardNumber::class -> mapOf("creditcard" to true)
                DecimalMax::class, Max::class -> mapOf("max" to "value")
                DecimalMin::class, Min::class -> mapOf("min" to "value")
                Digits::class -> mapOf("integerPrecision" to "integer", "fractionPrecision" to "fraction")
                Size::class, Length::class -> mapOf("minlength" to "min", "maxlength" to "max")
                Range::class -> mapOf("min" to "min", "max" to "max")
                DateTime::class -> mapOf("datetime" to "format")
                Series::class -> mapOf("series" to "type")
                else -> mapOf()
            }

}