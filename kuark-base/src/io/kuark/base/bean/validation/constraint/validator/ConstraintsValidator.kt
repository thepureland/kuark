package io.kuark.base.bean.validation.constraint.validator

import io.kuark.base.bean.validation.constraint.annotaions.*
import io.kuark.base.bean.validation.support.ValidationContext
import io.kuark.base.lang.reflect.getMemberProperty
import io.kuark.base.lang.reflect.getMemberPropertyValue
import io.kuark.base.support.Consts
import io.kuark.base.support.logic.AndOr
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
import javax.validation.ConstraintValidatorContext
import javax.validation.constraints.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import kotlin.reflect.full.declaredMemberProperties

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
        val annotations = getAnnotations(constraints)
        val bean = ValidationContext.get(context)
        if (constraints.andOr == AndOr.AND) {
            val failFast = ValidationContext.isFailFast()
            var pass = true
            annotations.forEach {
                pass = validate(it, value, bean, context)
                if (!pass) {
                    addViolation(context, it)
                    if (failFast) {
                        return false
                    }
                }
            }
            return pass
        } else { // 有一个约束成功就算通过，并且不受failFast影响
            var pass = false
            annotations.forEach {
                pass = validate(it, value, bean, context)
                if (pass) {
                    return true
                }
            }
            return pass
        }
    }

    companion object {
        fun getAnnotations(constraints: Constraints): List<Annotation> {
            // 获取定义的子约束
            val annotations = mutableListOf<Annotation>()
            var priorityAnnotation: Annotation? = null // 强制优先NotBlank、NotEmpty、NotNull、Null之一
            constraints.annotationClass.declaredMemberProperties.forEach {
                if (it.name != "order" && it.name != "andOr" && it.name != "message" && it.name != "groups" && it.name != "payload") {
                    val annotation = it.call(constraints) as Annotation
                    val message = annotation.annotationClass.getMemberPropertyValue(annotation, "message")
                    if (message != Constraints.MESSAGE) {
                        annotations.add(annotation)
                        if (annotation.annotationClass in setOf(NotBlank::class, NotEmpty::class, NotNull::class, Null::class)) {
                            priorityAnnotation = annotation
                        }
                    }
                }
            }

            // 根据指定的顺序排序子约束
            val result = if (constraints.order.isNotEmpty()) {
                val sequenceAnnotations = linkedSetOf<Annotation>() // 有指定顺序的子约束
                constraints.order.forEach { clazz ->
                    val annotation = annotations.firstOrNull { it.annotationClass == clazz }
                    if (annotation == null) {
                        error("Constraints约束sequence中指定的【$clazz】子约束未定义规则！")
                    } else {
                        sequenceAnnotations.add(annotation)
                    }
                }
                sequenceAnnotations.addAll(annotations) // 合并未指定顺序的子约束
                sequenceAnnotations.toList()
            } else {
                annotations
            }

            // 强制优先NotBlank、NotEmpty、NotNull、Null
            return if (priorityAnnotation != null) {
                val sequenceAnnotations = linkedSetOf<Annotation>()
                sequenceAnnotations.add(priorityAnnotation!!)
                sequenceAnnotations.addAll(result)
                sequenceAnnotations.toList()
            } else {
                result
            }
        }
    }

    private fun validate(
        annotation: Annotation,
        value: Any?,
        bean: Any?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value == null) {
            return annotation.annotationClass != NotNull::class && annotation.annotationClass != NotEmpty::class
                    && annotation.annotationClass != NotBlank::class
        }

        return when (annotation) {
            // javax.validation定义的约束
            is AssertFalse -> doValidate(AssertFalseValidator(), annotation, value, context)
            is AssertTrue -> doValidate(AssertTrueValidator(), annotation, value, context)
            is DecimalMax -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is DecimalMin -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is Digits -> {
                val validator = when (value) {
                    is CharSequence -> DigitsValidatorForCharSequence()
                    is Number -> DigitsValidatorForNumber()
                    is MonetaryAmount -> DigitsValidatorForMonetaryAmount()
                    else -> error("Digits约束注解不支持【${value::class}】类型的校验！")
                }
                doValidate(validator, annotation, value, context)
            }
            is Email -> doValidate(EmailValidator(), annotation, value, context)
            is Future -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is FutureOrPresent -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is Max -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is Min -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is Negative -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is NegativeOrZero -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is NotBlank -> doValidate(NotBlankValidator(), annotation, value, context)
            is NotEmpty -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is NotNull -> doValidate(NotNullValidator(), annotation, value, context)
            is Null -> doValidate(NullValidator(), annotation, value, context)
            is Past -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is PastOrPresent -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is Pattern -> doValidate(PatternValidator(), annotation, value, context)
            is Positive -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is PositiveOrZero -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }
            is Size -> {
                val validator = when (value) {
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
                }
                doValidate(validator, annotation, value, context)
            }


            // hibernate定义的约束
            is CodePointLength -> doValidate(CodePointLengthValidator(), annotation, value, context)
            is CreditCardNumber -> {
                val ignoreNonDigitCharacters = annotation.ignoreNonDigitCharacters
                val constructor = LuhnCheck::class.constructors.first()
                val luhnCheck = constructor.callBy(mapOf(constructor.parameters[3] to ignoreNonDigitCharacters))
                doValidate(LuhnCheckValidator(), luhnCheck, value, context)
            }
            is Currency -> {
                doValidate(CurrencyValidatorForMonetaryAmount(), annotation, value, context)
            }
            is EAN -> doValidate(EANValidator(), annotation, value, context)
            is ISBN -> doValidate(ISBNValidator(), annotation, value, context)
            is Length -> doValidate(LengthValidator(), annotation, value, context)
            is LuhnCheck -> doValidate(LuhnCheckValidator(), annotation, value, context)
            is Mod10Check -> doValidate(Mod10CheckValidator(), annotation, value, context)
            is Mod11Check -> doValidate(Mod11CheckValidator(), annotation, value, context)
            is ParameterScriptAssert ->
                doValidate(ParameterScriptAssertValidator(), annotation, value, context)
            is Range -> {
                val minConstructor = Min::class.constructors.first()
                val minAnnotation = minConstructor.callBy(mapOf(minConstructor.parameters[3] to annotation.min))
                val maxConstructor = Max::class.constructors.first()
                val maxAnnotation = maxConstructor.callBy(mapOf(maxConstructor.parameters[3] to annotation.max))
                when (value) {
                    is CharSequence -> doValidate(MinValidatorForCharSequence(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForCharSequence(), maxAnnotation, value, context)
                    is Double -> doValidate(MinValidatorForDouble(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForDouble(), maxAnnotation, value, context)
                    is Int -> doValidate(MinValidatorForInteger(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForInteger(), maxAnnotation, value, context)
                    is Long -> doValidate(MinValidatorForLong(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForLong(), maxAnnotation, value, context)
                    is Float -> doValidate(MinValidatorForFloat(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForFloat(), maxAnnotation, value, context)
                    is Byte -> doValidate(MinValidatorForByte(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForByte(), maxAnnotation, value, context)
                    is Short -> doValidate(MinValidatorForShort(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForShort(), maxAnnotation, value, context)
                    is BigDecimal -> doValidate(MinValidatorForBigDecimal(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForBigDecimal(), maxAnnotation, value, context)
                    is BigInteger -> doValidate(MinValidatorForBigInteger(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForBigInteger(), maxAnnotation, value, context)
                    is Number -> doValidate(MinValidatorForNumber(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForNumber(), maxAnnotation, value, context)
                    is MonetaryAmount -> doValidate(MinValidatorForMonetaryAmount(), minAnnotation, value, context)
                            && doValidate(MaxValidatorForMonetaryAmount(), maxAnnotation, value, context)
                    else -> error("Range约束注解不支持【${value::class}】类型的校验！")
                }
            }
//            is ScriptAssert -> doValidate(ScriptAssertValidator(), annotation, value, context) //kuark暂不支持
            is UniqueElements -> doValidate(UniqueElementsValidator(), annotation, value, context)
            is URL -> doValidate(URLValidator(), annotation, value, context)


            // kuark定义的约束
            is AtLeast -> doValidate(AtLeastValidator(), annotation, bean, context)
            is CnIdCardNo -> doValidate(CnIdCardNoValidator(), annotation, value, context)
            is Compare -> doValidate(CompareValidator(), annotation, value, context)
            is Custom -> doValidate(CustomValidator(), annotation, value, context)
            is DateTime -> doValidate(DateTimeValidator(), annotation, value, context)
            is DictEnumCode -> doValidate(DictEnumCodeValidator(), annotation, value, context)
            is NotNullOn -> doValidate(NotNullOnValidator(), annotation, value, context)
            is Series -> doValidate(SeriesValidator(), annotation, value, context)

            else -> error("Constraints约束不支持【${annotation.annotationClass}】作为其子约束！")
        }
    }


    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
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