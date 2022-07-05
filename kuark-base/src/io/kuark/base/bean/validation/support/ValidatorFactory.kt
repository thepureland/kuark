package io.kuark.base.bean.validation.support

import io.kuark.base.bean.validation.constraint.annotaions.*
import io.kuark.base.bean.validation.constraint.validator.*
import io.kuark.base.support.Consts
import org.hibernate.validator.constraints.*
import org.hibernate.validator.constraints.Currency
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
import javax.validation.constraints.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty


/**
 * 验证器工厂
 *
 * @author K
 * @since 1.0.0
 */
object ValidatorFactory {

    /**
     * 返回校验规则注解对应的验证器实例
     *
     * @param annotation 校验规则注解
     * @param value 待校验的值
     * @return 验证器列表(Range规则的验证器使用的是Min和Max的验证器共同校验)
     * @author K
     * @since 1.0.0
     */
    fun getValidator(annotation: Annotation, value: Any): List<ConstraintValidator<*, Any?>> {
        var validators: Any  = when (annotation) {
            // javax.validation定义的约束
            is AssertFalse -> AssertFalseValidator()
            is AssertTrue -> AssertTrueValidator()
            is DecimalMax -> {
                when (value) {
                    is CharSequence -> DecimalMaxValidatorForCharSequence()
                    is Double -> DecimalMaxValidatorForDouble()
                    is Int -> DecimalMaxValidatorForInteger()
                    is Long -> DecimalMaxValidatorForLong()
                    is Float -> DecimalMaxValidatorForFloat()
                    is Byte -> DecimalMaxValidatorForByte()
                    is Short -> DecimalMaxValidatorForShort()
                    is BigDecimal -> DecimalMaxValidatorForBigDecimal()
                    is BigInteger -> DecimalMaxValidatorForBigInteger()
                    is Number -> DecimalMaxValidatorForNumber()
                    is MonetaryAmount -> DecimalMaxValidatorForMonetaryAmount()
                    else -> error("DecimalMax约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }
            is DecimalMin -> {
                when (value) {
                    is CharSequence -> DecimalMinValidatorForCharSequence()
                    is Double -> DecimalMinValidatorForDouble()
                    is Int -> DecimalMinValidatorForInteger()
                    is Long -> DecimalMinValidatorForLong()
                    is Float -> DecimalMinValidatorForFloat()
                    is Byte -> DecimalMinValidatorForByte()
                    is Short -> DecimalMinValidatorForShort()
                    is BigDecimal -> DecimalMinValidatorForBigDecimal()
                    is BigInteger -> DecimalMinValidatorForBigInteger()
                    is Number -> DecimalMinValidatorForNumber()
                    is MonetaryAmount -> DecimalMinValidatorForMonetaryAmount()
                    else -> error("DecimalMin约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }
            is Digits -> {
                when (value) {
                    is CharSequence -> DigitsValidatorForCharSequence()
                    is Number -> DigitsValidatorForNumber()
                    is MonetaryAmount -> DigitsValidatorForMonetaryAmount()
                    else -> error("Digits约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }
            is Email -> EmailValidator().apply { initialize(annotation) }
            is Future -> {
                when (value) {
                    is LocalDate -> FutureValidatorForLocalDate()
                    is LocalDateTime -> FutureValidatorForLocalDateTime()
                    is LocalTime -> FutureValidatorForLocalTime()
                    is Instant -> FutureValidatorForInstant()
                    is Calendar -> FutureValidatorForCalendar()
                    is Date -> FutureValidatorForDate()
                    is HijrahDate -> FutureValidatorForHijrahDate()
                    is JapaneseDate -> FutureValidatorForJapaneseDate()
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
                }.apply { initialize(annotation) }
            }
            is FutureOrPresent -> {
                when (value) {
                    is LocalDate -> FutureOrPresentValidatorForLocalDate()
                    is LocalDateTime -> FutureOrPresentValidatorForLocalDateTime()
                    is LocalTime -> FutureOrPresentValidatorForLocalTime()
                    is Instant -> FutureOrPresentValidatorForInstant()
                    is Calendar -> FutureOrPresentValidatorForCalendar()
                    is Date -> FutureOrPresentValidatorForDate()
                    is HijrahDate -> FutureOrPresentValidatorForHijrahDate()
                    is JapaneseDate -> FutureOrPresentValidatorForJapaneseDate()
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
                }.apply { initialize(annotation) }
            }
            is Max -> {
                when (value) {
                    is CharSequence -> MaxValidatorForCharSequence()
                    is Double -> MaxValidatorForDouble()
                    is Int -> MaxValidatorForInteger()
                    is Long -> MaxValidatorForLong()
                    is Float -> MaxValidatorForFloat()
                    is Byte -> MaxValidatorForByte()
                    is Short -> MaxValidatorForShort()
                    is BigDecimal -> MaxValidatorForBigDecimal()
                    is BigInteger -> MaxValidatorForBigInteger()
                    is Number -> MaxValidatorForNumber()
                    is MonetaryAmount -> MaxValidatorForMonetaryAmount()
                    else -> error("Max约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }
            is Min -> {
                when (value) {
                    is CharSequence -> MinValidatorForCharSequence()
                    is Double -> MinValidatorForDouble()
                    is Int -> MinValidatorForInteger()
                    is Long -> MinValidatorForLong()
                    is Float -> MinValidatorForFloat()
                    is Byte -> MinValidatorForByte()
                    is Short -> MinValidatorForShort()
                    is BigDecimal -> MinValidatorForBigDecimal()
                    is BigInteger -> MinValidatorForBigInteger()
                    is Number -> MinValidatorForNumber()
                    is MonetaryAmount -> MinValidatorForMonetaryAmount()
                    else -> error("Min约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }
            is Negative -> {
                when (value) {
                    is CharSequence -> NegativeValidatorForCharSequence()
                    is Double -> NegativeValidatorForDouble()
                    is Int -> NegativeValidatorForInteger()
                    is Long -> NegativeValidatorForLong()
                    is Float -> NegativeValidatorForFloat()
                    is Byte -> NegativeValidatorForByte()
                    is Short -> NegativeValidatorForShort()
                    is BigDecimal -> NegativeValidatorForBigDecimal()
                    is BigInteger -> NegativeValidatorForBigInteger()
                    is Number -> NegativeValidatorForNumber()
                    is MonetaryAmount -> NegativeValidatorForMonetaryAmount()
                    else -> error("Negative约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }
            is NegativeOrZero -> {
                when (value) {
                    is CharSequence -> NegativeOrZeroValidatorForCharSequence()
                    is Double -> NegativeOrZeroValidatorForDouble()
                    is Int -> NegativeOrZeroValidatorForInteger()
                    is Long -> NegativeOrZeroValidatorForLong()
                    is Float -> NegativeOrZeroValidatorForFloat()
                    is Byte -> NegativeOrZeroValidatorForByte()
                    is Short -> NegativeOrZeroValidatorForShort()
                    is BigDecimal -> NegativeOrZeroValidatorForBigDecimal()
                    is BigInteger -> NegativeOrZeroValidatorForBigInteger()
                    is Number -> NegativeOrZeroValidatorForNumber()
                    is MonetaryAmount -> NegativeOrZeroValidatorForMonetaryAmount()
                    else -> error("NegativeOrZero约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }
            is NotBlank -> NotBlankValidator()
            is NotEmpty -> {
                when (value) {
                    is CharSequence -> NotEmptyValidatorForCharSequence()
                    is Array<*> -> NotEmptyValidatorForArray()
                    is Collection<*> -> NotEmptyValidatorForCollection()
                    is DoubleArray -> NotEmptyValidatorForArraysOfDouble()
                    is IntArray -> NotEmptyValidatorForArraysOfInt()
                    is LongArray -> NotEmptyValidatorForArraysOfLong()
                    is CharArray -> NotEmptyValidatorForArraysOfChar()
                    is FloatArray -> NotEmptyValidatorForArraysOfFloat()
                    is BooleanArray -> NotEmptyValidatorForArraysOfBoolean()
                    is ByteArray -> NotEmptyValidatorForArraysOfByte()
                    is ShortArray -> NotEmptyValidatorForArraysOfShort()
                    is Map<*, *> -> NotEmptyValidatorForMap()
                    else -> error("NotEmpty约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }
            is NotNull -> NotNullValidator()
            is Null -> NullValidator()
            is Past -> {
                when (value) {
                    is LocalDate -> PastValidatorForLocalDate()
                    is LocalDateTime -> PastValidatorForLocalDateTime()
                    is LocalTime -> PastValidatorForLocalTime()
                    is Instant -> PastValidatorForInstant()
                    is Calendar -> PastValidatorForCalendar()
                    is Date -> PastValidatorForDate()
                    is HijrahDate -> PastValidatorForHijrahDate()
                    is JapaneseDate -> PastValidatorForJapaneseDate()
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
                }.apply { initialize(annotation) }
            }
            is PastOrPresent -> {
                when (value) {
                    is LocalDate -> PastOrPresentValidatorForLocalDate()
                    is LocalDateTime -> PastOrPresentValidatorForLocalDateTime()
                    is LocalTime -> PastOrPresentValidatorForLocalTime()
                    is Instant -> PastOrPresentValidatorForInstant()
                    is Calendar -> PastOrPresentValidatorForCalendar()
                    is Date -> PastOrPresentValidatorForDate()
                    is HijrahDate -> PastOrPresentValidatorForHijrahDate()
                    is JapaneseDate -> PastOrPresentValidatorForJapaneseDate()
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
                }.apply { initialize(annotation) }
            }
            is Pattern -> PatternValidator().apply { initialize(annotation) }
            is Positive -> {
                when (value) {
                    is CharSequence -> PositiveValidatorForCharSequence()
                    is Double -> PositiveValidatorForDouble()
                    is Int -> PositiveValidatorForInteger()
                    is Long -> PositiveValidatorForLong()
                    is Float -> PositiveValidatorForFloat()
                    is Byte -> PositiveValidatorForByte()
                    is Short -> PositiveValidatorForShort()
                    is BigDecimal -> PositiveValidatorForBigDecimal()
                    is BigInteger -> PositiveValidatorForBigInteger()
                    is Number -> PositiveValidatorForNumber()
                    is MonetaryAmount -> PositiveValidatorForMonetaryAmount()
                    else -> error("Positive约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }
            is PositiveOrZero -> {
                when (value) {
                    is CharSequence -> PositiveOrZeroValidatorForCharSequence()
                    is Double -> PositiveOrZeroValidatorForDouble()
                    is Int -> PositiveOrZeroValidatorForInteger()
                    is Long -> PositiveOrZeroValidatorForLong()
                    is Float -> PositiveOrZeroValidatorForFloat()
                    is Byte -> PositiveOrZeroValidatorForByte()
                    is Short -> PositiveOrZeroValidatorForShort()
                    is BigDecimal -> PositiveOrZeroValidatorForBigDecimal()
                    is BigInteger -> PositiveOrZeroValidatorForBigInteger()
                    is Number -> PositiveOrZeroValidatorForNumber()
                    is MonetaryAmount -> PositiveOrZeroValidatorForMonetaryAmount()
                    else -> error("PositiveOrZero约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }
            is Size -> {
                when (value) {
                    is CharSequence -> SizeValidatorForCharSequence()
                    is Array<*> -> SizeValidatorForArray()
                    is Collection<*> -> SizeValidatorForCollection()
                    is DoubleArray -> SizeValidatorForArraysOfDouble()
                    is IntArray -> SizeValidatorForArraysOfInt()
                    is LongArray -> SizeValidatorForArraysOfLong()
                    is CharArray -> SizeValidatorForArraysOfChar()
                    is FloatArray -> SizeValidatorForArraysOfFloat()
                    is BooleanArray -> SizeValidatorForArraysOfBoolean()
                    is ByteArray -> SizeValidatorForArraysOfByte()
                    is ShortArray -> SizeValidatorForArraysOfShort()
                    is Map<*, *> -> SizeValidatorForMap()
                    else -> error("Size约束注解不支持【${value::class}】类型的校验！")
                }.apply { initialize(annotation) }
            }


            // hibernate定义的约束
            is CodePointLength -> CodePointLengthValidator().apply { initialize(annotation) }
            is CreditCardNumber -> {
                val ignoreNonDigitCharacters = annotation.ignoreNonDigitCharacters
                val constructor = LuhnCheck::class.constructors.first()
                val luhnCheck = constructor.callBy(mapOf(constructor.parameters[3] to ignoreNonDigitCharacters))
                getValidator(luhnCheck, value)
            }
            is Currency -> CurrencyValidatorForMonetaryAmount().apply { initialize(annotation) }
            is EAN -> EANValidator().apply { initialize(annotation) }
            is ISBN -> ISBNValidator().apply { initialize(annotation) }
            is Length -> LengthValidator().apply { initialize(annotation) }
            is LuhnCheck -> LuhnCheckValidator().apply { initialize(annotation) }
            is Mod10Check -> Mod10CheckValidator().apply { initialize(annotation) }
            is Mod11Check -> Mod11CheckValidator().apply { initialize(annotation) }
            is ParameterScriptAssert -> ParameterScriptAssertValidator().apply { initialize(annotation) }
            is Range -> {
                val minConstructor = Min::class.constructors.first()
                val minAnnotation = minConstructor.callBy(mapOf(minConstructor.parameters[3] to annotation.min))
                val maxConstructor = Max::class.constructors.first()
                val maxAnnotation = maxConstructor.callBy(mapOf(maxConstructor.parameters[3] to annotation.max))
                listOf<ConstraintValidator<*, *>>(
                    getValidator(minAnnotation, value).first(),
                    getValidator(maxAnnotation, value).first(),
                )
            }
//            is ScriptAssert -> doValidate(ScriptAssertValidator(), annotation, value, context) //kuark暂不支持
            is UniqueElements -> UniqueElementsValidator()
            is URL -> URLValidator().apply { initialize(annotation) }


            // kuark定义的约束
            is AtLeast -> AtLeastValidator().apply { initialize(annotation) }
            is CnIdCardNo -> CnIdCardNoValidator().apply { initialize(annotation) }
            is Compare -> CompareValidator().apply { initialize(annotation) }
            is Custom -> CustomValidator().apply { initialize(annotation) }
            is DateTime -> DateTimeValidator().apply { initialize(annotation) }
            is DictEnumCode -> DictEnumCodeValidator().apply { initialize(annotation) }
            is NotNullOn -> NotNullOnValidator().apply { initialize(annotation) }
            is Series -> SeriesValidator().apply { initialize(annotation) }

            else -> listOf<ConstraintValidator<*, *>>()
        }
        if (validators !is List<*>) {
            validators = listOf(validators)
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return validators as List<ConstraintValidator<*, Any?>>
    }

}