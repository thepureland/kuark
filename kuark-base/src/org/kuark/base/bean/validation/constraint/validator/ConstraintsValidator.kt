package org.kuark.base.bean.validation.constraint.validator

import org.hibernate.validator.constraints.CreditCardNumber
import org.hibernate.validator.constraints.LuhnCheck
import org.hibernate.validator.constraints.Range
import org.hibernate.validator.internal.constraintvalidators.bv.*
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator
import org.hibernate.validator.internal.constraintvalidators.bv.money.*
import org.hibernate.validator.internal.constraintvalidators.bv.notempty.*
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.*
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.decimal.*
import org.hibernate.validator.internal.constraintvalidators.bv.number.sign.*
import org.hibernate.validator.internal.constraintvalidators.bv.size.*
import org.hibernate.validator.internal.constraintvalidators.bv.time.future.*
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.*
import org.hibernate.validator.internal.constraintvalidators.bv.time.past.*
import org.hibernate.validator.internal.constraintvalidators.bv.time.pastorpresent.*
import org.hibernate.validator.internal.constraintvalidators.hv.*
import org.kuark.base.bean.validation.constraint.annotaions.Constraints
import org.kuark.base.bean.validation.support.ConstraintType
import org.kuark.base.bean.validation.support.ValidationContext
import org.kuark.base.lang.reflect.getMemberProperty
import org.kuark.base.support.logic.AndOr
import java.math.BigDecimal
import java.math.BigInteger
import java.time.*
import java.time.chrono.HijrahDate
import java.time.chrono.JapaneseDate
import java.time.chrono.MinguoDate
import java.time.chrono.ThaiBuddhistDate
import java.util.*
import javax.money.MonetaryAmount
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.constraints.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.starProjectedType

/**
 * Constraints约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class ConstraintsValidator : ConstraintValidator<Constraints, Any?> {

    private lateinit var constraints: Constraints

    override fun initialize(constraints: Constraints) {
        this.constraints = constraints
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        val annotations = linkedMapOf<ConstraintType, Annotation>()
        constraints.values.forEach { type ->
            val annotationType = type.annotationClass.starProjectedType
            val prop = constraints.annotationClass.declaredMemberProperties.first {
                it.returnType == annotationType
            }
            annotations[type] = prop.call(constraints) as Annotation
        }

        val bean = ValidationContext.get(context)
        if (constraints.andOr == AndOr.AND) {
            val failFast = ValidationContext.isFailFast()
            var pass = true
            annotations.forEach {
                pass = validate(it.key, it.value, value, bean, context)
                if (!pass) {
                    addViolation(context, it.value)
                    if (failFast) {
                        return false
                    }
                }
            }
            return pass
        } else { // 有一个约束成功就算通过，并且不受failFast影响
            var pass = false
            annotations.forEach {
                pass = validate(it.key, it.value, value, bean, context)
                if (pass) {
                    return true
                }
            }
            return pass
        }
    }

    private fun validate(
        constraintType: ConstraintType,
        annotation: Annotation,
        value: Any?,
        bean: Any?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value == null) {
            return annotation.annotationClass != NotNull::class && annotation.annotationClass != NotEmpty::class
                    && annotation.annotationClass != NotBlank::class
        }

        return when (constraintType) {
            // javax.validation定义的约束
            ConstraintType.AssertFalse -> doValidate(AssertFalseValidator(), annotation, value, context)
            ConstraintType.AssertTrue -> doValidate(AssertTrueValidator(), annotation, value, context)
            ConstraintType.DecimalMax -> {
                val validator = when (value) {
                    is BigDecimal -> DecimalMaxValidatorForBigDecimal()
                    is BigInteger -> DecimalMaxValidatorForBigInteger()
                    is Byte -> DecimalMaxValidatorForByte()
                    is CharSequence -> DecimalMaxValidatorForCharSequence()
                    is Double -> DecimalMaxValidatorForDouble()
                    is Float -> DecimalMaxValidatorForFloat()
                    is Integer -> DecimalMaxValidatorForInteger()
                    is Long -> DecimalMaxValidatorForLong()
                    is Number -> DecimalMaxValidatorForNumber()
                    is Short -> DecimalMaxValidatorForShort()
                    is MonetaryAmount -> DecimalMaxValidatorForMonetaryAmount()
                    else -> error("DecimalMax约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.DecimalMin -> {
                val validator = when (value) {
                    is BigDecimal -> DecimalMinValidatorForBigDecimal()
                    is BigInteger -> DecimalMinValidatorForBigInteger()
                    is Byte -> DecimalMinValidatorForByte()
                    is CharSequence -> DecimalMinValidatorForCharSequence()
                    is Double -> DecimalMinValidatorForDouble()
                    is Float -> DecimalMinValidatorForFloat()
                    is Integer -> DecimalMinValidatorForInteger()
                    is Long -> DecimalMinValidatorForLong()
                    is Number -> DecimalMinValidatorForNumber()
                    is Short -> DecimalMinValidatorForShort()
                    is MonetaryAmount -> DecimalMinValidatorForMonetaryAmount()
                    else -> error("DecimalMin约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.Digits -> {
                val validator = when (value) {
                    is CharSequence -> DigitsValidatorForCharSequence()
                    is Number -> DigitsValidatorForNumber()
                    is MonetaryAmount -> DigitsValidatorForMonetaryAmount()
                    else -> error("Digits约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.Email -> doValidate(EmailValidator(), annotation, value, context)
            ConstraintType.Future -> {
                val validator = when (value) {
                    is Calendar -> FutureValidatorForCalendar()
                    is Date -> FutureValidatorForDate()
                    is HijrahDate -> FutureValidatorForHijrahDate()
                    is Instant -> FutureValidatorForInstant()
                    is JapaneseDate -> FutureValidatorForJapaneseDate()
                    is LocalDate -> FutureValidatorForLocalDate()
                    is LocalDateTime -> FutureValidatorForLocalDateTime()
                    is LocalTime -> FutureValidatorForLocalTime()
                    is MinguoDate -> FutureValidatorForMinguoDate()
                    is MonthDay -> FutureValidatorForMonthDay()
                    is OffsetDateTime -> FutureValidatorForOffsetDateTime()
                    is OffsetTime -> FutureValidatorForOffsetTime()
//                    is ReadableInstant -> FutureValidatorForReadableInstant()
//                    is ReadablePartial -> FutureValidatorForReadablePartial()
                    is ThaiBuddhistDate -> FutureValidatorForThaiBuddhistDate()
                    is Year -> FutureValidatorForYear()
                    is YearMonth -> FutureValidatorForYearMonth()
                    is ZonedDateTime -> FutureValidatorForZonedDateTime()
                    else -> error("Future约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.FutureOrPresent -> {
                val validator = when (value) {
                    is Calendar -> FutureOrPresentValidatorForCalendar()
                    is Date -> FutureOrPresentValidatorForDate()
                    is HijrahDate -> FutureOrPresentValidatorForHijrahDate()
                    is Instant -> FutureOrPresentValidatorForInstant()
                    is JapaneseDate -> FutureOrPresentValidatorForJapaneseDate()
                    is LocalDate -> FutureOrPresentValidatorForLocalDate()
                    is LocalDateTime -> FutureOrPresentValidatorForLocalDateTime()
                    is LocalTime -> FutureOrPresentValidatorForLocalTime()
                    is MinguoDate -> FutureOrPresentValidatorForMinguoDate()
                    is MonthDay -> FutureOrPresentValidatorForMonthDay()
                    is OffsetDateTime -> FutureOrPresentValidatorForOffsetDateTime()
                    is OffsetTime -> FutureOrPresentValidatorForOffsetTime()
//                    is ReadableInstant -> FutureOrPresentValidatorForReadableInstant()
//                    is ReadablePartial -> FutureOrPresentValidatorForReadablePartial()
                    is ThaiBuddhistDate -> FutureOrPresentValidatorForThaiBuddhistDate()
                    is Year -> FutureOrPresentValidatorForYear()
                    is YearMonth -> FutureOrPresentValidatorForYearMonth()
                    is ZonedDateTime -> FutureOrPresentValidatorForZonedDateTime()
                    else -> error("FutureOrPresent约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.Max -> {
                val validator = when (value) {
                    is BigDecimal -> MaxValidatorForBigDecimal()
                    is BigInteger -> MaxValidatorForBigInteger()
                    is Byte -> MaxValidatorForByte()
                    is CharSequence -> MaxValidatorForCharSequence()
                    is Double -> MaxValidatorForDouble()
                    is Float -> MaxValidatorForFloat()
                    is Integer -> MaxValidatorForInteger()
                    is Long -> MaxValidatorForLong()
                    is Short -> MaxValidatorForShort()
                    is Number -> MaxValidatorForNumber()
                    is MonetaryAmount -> MaxValidatorForMonetaryAmount()
                    else -> error("Max约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.Min -> {
                val validator = when (value) {
                    is BigDecimal -> MinValidatorForBigDecimal()
                    is BigInteger -> MinValidatorForBigInteger()
                    is Byte -> MinValidatorForByte()
                    is CharSequence -> MinValidatorForCharSequence()
                    is Double -> MinValidatorForDouble()
                    is Float -> MinValidatorForFloat()
                    is Integer -> MinValidatorForInteger()
                    is Long -> MinValidatorForLong()
                    is Short -> MinValidatorForShort()
                    is Number -> MinValidatorForNumber()
                    is MonetaryAmount -> MinValidatorForMonetaryAmount()
                    else -> error("Min约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.Negative -> {
                val validator = when (value) {
                    is BigDecimal -> NegativeValidatorForBigDecimal()
                    is BigInteger -> NegativeValidatorForBigInteger()
                    is Byte -> NegativeValidatorForByte()
                    is CharSequence -> NegativeValidatorForCharSequence()
                    is Double -> NegativeValidatorForDouble()
                    is Float -> NegativeValidatorForFloat()
                    is Integer -> NegativeValidatorForInteger()
                    is Long -> NegativeValidatorForLong()
                    is Short -> NegativeValidatorForShort()
                    is Number -> NegativeValidatorForNumber()
                    is MonetaryAmount -> NegativeValidatorForMonetaryAmount()
                    else -> error("Negative约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.NegativeOrZero -> {
                val validator = when (value) {
                    is BigDecimal -> NegativeOrZeroValidatorForBigDecimal()
                    is BigInteger -> NegativeOrZeroValidatorForBigInteger()
                    is Byte -> NegativeOrZeroValidatorForByte()
                    is CharSequence -> NegativeOrZeroValidatorForCharSequence()
                    is Double -> NegativeOrZeroValidatorForDouble()
                    is Float -> NegativeOrZeroValidatorForFloat()
                    is Integer -> NegativeOrZeroValidatorForInteger()
                    is Long -> NegativeOrZeroValidatorForLong()
                    is Short -> NegativeOrZeroValidatorForShort()
                    is Number -> NegativeOrZeroValidatorForNumber()
                    is MonetaryAmount -> NegativeOrZeroValidatorForMonetaryAmount()
                    else -> error("NegativeOrZero约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.NotBlank -> doValidate(NotBlankValidator(), annotation, value, context)
            ConstraintType.NotEmpty -> {
                val validator = when (value) {
                    is Array<*> -> NotEmptyValidatorForArray()
                    is BooleanArray -> NotEmptyValidatorForArraysOfBoolean()
                    is ByteArray -> NotEmptyValidatorForArraysOfByte()
                    is CharArray -> NotEmptyValidatorForArraysOfChar()
                    is DoubleArray -> NotEmptyValidatorForArraysOfDouble()
                    is FloatArray -> NotEmptyValidatorForArraysOfFloat()
                    is IntArray -> NotEmptyValidatorForArraysOfInt()
                    is LongArray -> NotEmptyValidatorForArraysOfLong()
                    is ShortArray -> NotEmptyValidatorForArraysOfShort()
                    is CharSequence -> NotEmptyValidatorForCharSequence()
                    is Collection<*> -> NotEmptyValidatorForCollection()
                    is Map<*, *> -> NotEmptyValidatorForMap()
                    else -> error("NotEmpty约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.NotNull -> doValidate(NotNullValidator(), annotation, value, context)
            ConstraintType.Null -> doValidate(NullValidator(), annotation, value, context)
            ConstraintType.Past -> {
                val validator = when (value) {
                    is Calendar -> PastValidatorForCalendar()
                    is Date -> PastValidatorForDate()
                    is HijrahDate -> PastValidatorForHijrahDate()
                    is Instant -> PastValidatorForInstant()
                    is JapaneseDate -> PastValidatorForJapaneseDate()
                    is LocalDate -> PastValidatorForLocalDate()
                    is LocalDateTime -> PastValidatorForLocalDateTime()
                    is LocalTime -> PastValidatorForLocalTime()
                    is MinguoDate -> PastValidatorForMinguoDate()
                    is MonthDay -> PastValidatorForMonthDay()
                    is OffsetDateTime -> PastValidatorForOffsetDateTime()
                    is OffsetTime -> PastValidatorForOffsetTime()
//                    is ReadableInstant -> PastValidatorForReadableInstant()
//                    is ReadablePartial -> PastValidatorForReadablePartial()
                    is ThaiBuddhistDate -> PastValidatorForThaiBuddhistDate()
                    is Year -> PastValidatorForYear()
                    is YearMonth -> PastValidatorForYearMonth()
                    is ZonedDateTime -> PastValidatorForZonedDateTime()
                    else -> error("Past约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.PastOrPresent -> {
                val validator = when (value) {
                    is Calendar -> PastOrPresentValidatorForCalendar()
                    is Date -> PastOrPresentValidatorForDate()
                    is HijrahDate -> PastOrPresentValidatorForHijrahDate()
                    is Instant -> PastOrPresentValidatorForInstant()
                    is JapaneseDate -> PastOrPresentValidatorForJapaneseDate()
                    is LocalDate -> PastOrPresentValidatorForLocalDate()
                    is LocalDateTime -> PastOrPresentValidatorForLocalDateTime()
                    is LocalTime -> PastOrPresentValidatorForLocalTime()
                    is MinguoDate -> PastOrPresentValidatorForMinguoDate()
                    is MonthDay -> PastOrPresentValidatorForMonthDay()
                    is OffsetDateTime -> PastOrPresentValidatorForOffsetDateTime()
                    is OffsetTime -> PastOrPresentValidatorForOffsetTime()
//                    is ReadableInstant -> PastOrPresentValidatorForReadableInstant()
//                    is ReadablePartial -> PastOrPresentValidatorForReadablePartial()
                    is ThaiBuddhistDate -> PastOrPresentValidatorForThaiBuddhistDate()
                    is Year -> PastOrPresentValidatorForYear()
                    is YearMonth -> PastOrPresentValidatorForYearMonth()
                    is ZonedDateTime -> PastOrPresentValidatorForZonedDateTime()
                    else -> error("PastOrPresent约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.Pattern -> doValidate(PatternValidator(), annotation, value, context)
            ConstraintType.Positive -> {
                val validator = when (value) {
                    is BigDecimal -> PositiveValidatorForBigDecimal()
                    is BigInteger -> PositiveValidatorForBigInteger()
                    is Byte -> PositiveValidatorForByte()
                    is CharSequence -> PositiveValidatorForCharSequence()
                    is Double -> PositiveValidatorForDouble()
                    is Float -> PositiveValidatorForFloat()
                    is Integer -> PositiveValidatorForInteger()
                    is Long -> PositiveValidatorForLong()
                    is Short -> PositiveValidatorForShort()
                    is Number -> PositiveValidatorForNumber()
                    is MonetaryAmount -> PositiveValidatorForMonetaryAmount()
                    else -> error("Positive约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.PositiveOrZero -> {
                val validator = when (value) {
                    is BigDecimal -> PositiveOrZeroValidatorForBigDecimal()
                    is BigInteger -> PositiveOrZeroValidatorForBigInteger()
                    is Byte -> PositiveOrZeroValidatorForByte()
                    is CharSequence -> PositiveOrZeroValidatorForCharSequence()
                    is Double -> PositiveOrZeroValidatorForDouble()
                    is Float -> PositiveOrZeroValidatorForFloat()
                    is Integer -> PositiveOrZeroValidatorForInteger()
                    is Long -> PositiveOrZeroValidatorForLong()
                    is Short -> PositiveOrZeroValidatorForShort()
                    is Number -> PositiveOrZeroValidatorForNumber()
                    is MonetaryAmount -> PositiveOrZeroValidatorForMonetaryAmount()
                    else -> error("PositiveOrZero约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            ConstraintType.Size -> {
                val validator = when (value) {
                    is Array<*> -> SizeValidatorForArray()
                    is BooleanArray -> SizeValidatorForArraysOfBoolean()
                    is ByteArray -> SizeValidatorForArraysOfByte()
                    is CharArray -> SizeValidatorForArraysOfChar()
                    is DoubleArray -> SizeValidatorForArraysOfDouble()
                    is FloatArray -> SizeValidatorForArraysOfFloat()
                    is IntArray -> SizeValidatorForArraysOfInt()
                    is LongArray -> SizeValidatorForArraysOfLong()
                    is ShortArray -> SizeValidatorForArraysOfShort()
                    is CharSequence -> SizeValidatorForCharSequence()
                    is Collection<*> -> SizeValidatorForCollection()
                    is Map<*, *> -> SizeValidatorForMap()
                    else -> error("Size约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }


            // hibernate定义的约束
            ConstraintType.CodePointLength -> doValidate(CodePointLengthValidator(), annotation, value, context)
            ConstraintType.CreditCardNumber -> {
                val ignoreNonDigitCharacters = (annotation as CreditCardNumber).ignoreNonDigitCharacters
                val constructor = LuhnCheck::class.constructors.first()
                val luhnCheck = constructor.callBy(mapOf(constructor.parameters[3] to ignoreNonDigitCharacters))
                doValidate(LuhnCheckValidator(), luhnCheck, value, context)
            }
            ConstraintType.Currency -> {
                doValidate(CurrencyValidatorForMonetaryAmount(), annotation, value, context)
            }
            ConstraintType.EAN -> doValidate(EANValidator(), annotation, value, context)
            ConstraintType.ISBN -> doValidate(ISBNValidator(), annotation, value, context)
            ConstraintType.Length -> doValidate(LengthValidator(), annotation, value, context)
            ConstraintType.LuhnCheck -> doValidate(LuhnCheckValidator(), annotation, value, context)
            ConstraintType.Mod10Check -> doValidate(Mod10CheckValidator(), annotation, value, context)
            ConstraintType.Mod11Check -> doValidate(Mod11CheckValidator(), annotation, value, context)
            ConstraintType.ParameterScriptAssert ->
                doValidate(ParameterScriptAssertValidator(), annotation, value, context)
            ConstraintType.Range -> {
                annotation as Range
                val minConstructor = Min::class.constructors.first()
                val minAnnotation = minConstructor.callBy(mapOf(minConstructor.parameters[3] to annotation.min))
                val maxConstructor = Max::class.constructors.first()
                val maxAnnotation = maxConstructor.callBy(mapOf(maxConstructor.parameters[3] to annotation.max))
                when (value) {
                    is BigDecimal -> doValidate(MinValidatorForBigDecimal(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForBigDecimal(), maxAnnotation, value, context)
                    is BigInteger -> doValidate(MinValidatorForBigInteger(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForBigInteger(), maxAnnotation, value, context)
                    is Byte -> doValidate(MinValidatorForByte(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForByte(), maxAnnotation, value, context)
                    is CharSequence -> doValidate(MinValidatorForCharSequence(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForCharSequence(), maxAnnotation, value, context)
                    is Double -> doValidate(MinValidatorForDouble(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForDouble(), maxAnnotation, value, context)
                    is Float -> doValidate(MinValidatorForFloat(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForFloat(), maxAnnotation, value, context)
                    is Integer -> doValidate(MinValidatorForInteger(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForInteger(), maxAnnotation, value, context)
                    is Long -> doValidate(MinValidatorForLong(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForLong(), maxAnnotation, value, context)
                    is Short -> doValidate(MinValidatorForShort(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForShort(), maxAnnotation, value, context)
                    is Number -> doValidate(MinValidatorForNumber(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForNumber(), maxAnnotation, value, context)
                    else -> error("Range约束注解不支持【${value::class}】类型的校验！")
                }
            }
            ConstraintType.ScriptAssert -> doValidate(ScriptAssertValidator(), annotation, value, context)
            ConstraintType.UniqueElements -> doValidate(UniqueElementsValidator(), annotation, value, context)
            ConstraintType.URL -> doValidate(URLValidator(), annotation, value, context)


            // kuark定义的约束
            ConstraintType.AtLeast -> doValidate(AtLeastValidator(), annotation, bean, context)
            ConstraintType.CnIdCardNo -> doValidate(CnIdCardNoValidator(), annotation, value, context)
            ConstraintType.Compare -> doValidate(CompareValidator(), annotation, value, context)
            ConstraintType.CustomConstraint -> doValidate(CustomConstraintValidator(), annotation, value, context)
            ConstraintType.DateTime -> doValidate(DateTimeValidator(), annotation, value, context)
            ConstraintType.DictEnumCode -> doValidate(DictEnumCodeValidator(), annotation, value, context)
            ConstraintType.Each -> doValidate(EachValidator(), annotation, value, context)
            ConstraintType.NotNullOn -> doValidate(NotNullOnValidator(), annotation, value, context)
            ConstraintType.Series -> doValidate(SeriesValidator(), annotation, value, context)
        }
    }


    private fun doValidate(
        validator: Any, annotation: Annotation, value: Any?, context: ConstraintValidatorContext
    ): Boolean =
        with(validator as ConstraintValidator<Annotation, Any?>) {
            initialize(annotation)
            isValid(value, context)
        }

    private fun addViolation(context: ConstraintValidatorContext, annotation: Annotation) {
        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(
            annotation.annotationClass.getMemberProperty("message").call(annotation) as String
        ).addConstraintViolation()
    }

}