package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.hibernate.validator.constraints.CreditCardNumber
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import org.kuark.base.bean.validation.constraint.annotaions.DateTime
import org.kuark.base.bean.validation.constraint.annotaions.Series
import org.kuark.base.bean.validation.teminal.convert.converter.AbstractConstraintConverter
import javax.validation.constraints.*
import kotlin.reflect.KClass

/**
 * 默认的约束注解->终端约束转换器
 *
 * @author K
 * @since 1.0.0
 */
class DefaultConstaintJsConverter(annotation: Annotation) : AbstractConstraintConverter(annotation) {

    override fun getRule(constraintAnnotation: Annotation): MutableMap<String, Any> =
        when (constraintAnnotation.annotationClass) {
            NotNull::class, NotEmpty::class, NotBlank::class -> mutableMapOf("required" to true)
            Email::class -> mutableMapOf("email" to true)
            CreditCardNumber::class -> mutableMapOf("creditcard" to true)
            DecimalMax::class, Max::class -> mutableMapOf("max" to "value")
            DecimalMin::class, Min::class -> mutableMapOf("min" to "value")
            Digits::class -> mutableMapOf("integerPrecision" to "integer", "fractionPrecision" to "fraction")
            Size::class, Length::class -> mutableMapOf("minlength" to "min", "maxlength" to "max")
            Range::class -> mutableMapOf("min" to "min", "max" to "max")
            DateTime::class -> mutableMapOf("datetime" to "format")
            Series::class -> mutableMapOf("series" to "type")
            else -> mutableMapOf()
        }

}