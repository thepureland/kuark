package io.kuark.base.bean.validation.constraint.validator

import io.kuark.base.bean.validation.constraint.annotaions.Series
import io.kuark.base.bean.validation.support.SeriesType
import io.kuark.base.lang.collections.subarray
import java.math.BigDecimal
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Series约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class SeriesValidator : ConstraintValidator<Series, Any?> {

    private lateinit var series: Series

    override fun initialize(series: Series) {
        this.series = series
        if (series.step < 0.0) {
            error("@Series约束注解的step不能为负数！")
        }
        if (series.size < 0) {
            error("@Series约束注解的size不能为负数！")
        }
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        if (value is Array<*> || value is List<*>) {
            var values = if (value is Array<*>) {
                value.toList()
            } else {
                value as List<*>
            }
            if (values.size <= 1) {
                return true
            }
            if (series.size != 0 && values.size != series.size) {
                return false
            }
            values.forEach {
                it ?: error("@Series约束注解限制数组中每个元素均不能为null！数组为：$value")
            }

            // 将数组元素全部转为String，方便用BigDecimal进行高精度运算
            values = values.map { BigDecimal(it.toString()) }
            return validate(series.type, series.step, *values.toTypedArray())
        } else {
            error("@Series约束注解只能设置在返回值类型为Array或List的get方法上！")
        }
    }

    private fun validate(type: SeriesType, step: Double, vararg values: BigDecimal): Boolean {
        return when (type) {
            SeriesType.INC_DIFF -> {
                var preValue: BigDecimal? = null
                for (value in values) {
                    if (preValue != null) {
                        if (step == 0.0) { // 不应用步进
                            if (preValue >= value) {
                                return false
                            }
                        } else {
                            if (preValue + BigDecimal(step) != value) {
                                return false
                            }
                        }
                    }
                    preValue = value
                }
                true
            }
            SeriesType.DESC_DIFF -> {
                validate(SeriesType.INC_DIFF, step, *values.reversed().toTypedArray())
            }
            SeriesType.INC_DIFF_DESC_DIFF -> {
                val maxValueIndex = values.indexOf(values.maxOrNull())
                if (maxValueIndex == values.lastIndex) {
                    return false
                }
                val incDiffValues = values.subarray(0, maxValueIndex + 1)
                val incDiffPass = validate(SeriesType.INC_DIFF, step, *incDiffValues)
                if (incDiffPass) {
                    val descDiffValues = values.subarray(maxValueIndex, values.size)
                    validate(SeriesType.DESC_DIFF, step, *descDiffValues)
                } else {
                    false
                }
            }
            SeriesType.DESC_DIFF_INC_DIFF -> {
                val minValueIndex = values.indexOf(values.minOrNull())
                if (minValueIndex == values.lastIndex) {
                    return false
                }
                val descDiffValues = values.subarray(0, minValueIndex + 1)
                val descDiffPass = validate(SeriesType.DESC_DIFF, step, *descDiffValues)
                if (descDiffPass) {
                    val incDiffValues = values.subarray(minValueIndex, values.size)
                    validate(SeriesType.INC_DIFF, step, *incDiffValues)
                } else {
                    false
                }
            }
            SeriesType.DIFF -> {
                val diff = values.toSet().size == values.size
                if (!diff) {
                    return false
                } else if (step != 0.0) {
                    var preValue: BigDecimal? = null
                    for (value in values) {
                        if (preValue != null) {
                            if ((preValue - value).abs() != BigDecimal(step)) {
                                return false
                            }
                        }
                        preValue = value
                    }
                }
                true
            }
            SeriesType.INC_EQ -> {
                var preValue: BigDecimal? = null
                for (value in values) {
                    if (preValue != null) {
                        if (step == 0.0) { // 不应用步进
                            if (preValue > value) {
                                return false
                            }
                        } else {
                            if (preValue != value && preValue + BigDecimal(step) != value) {
                                return false
                            }
                        }
                    }
                    preValue = value
                }
                true
            }
            SeriesType.DESC_EQ -> {
                validate(SeriesType.INC_EQ, step, *values.reversed().toTypedArray())
            }
            SeriesType.INC_EQ_DESC_EQ -> {
                val maxValue = values.maxOrNull()
                val maxValueStartIndex = values.indexOf(maxValue)
                if (maxValueStartIndex == 0 || maxValueStartIndex == values.lastIndex) {
                    return false
                }
                var maxValueEndIndex = maxValueStartIndex
                for (index in maxValueStartIndex until values.lastIndex) {
                    if (values[index] == maxValue) {
                        maxValueEndIndex = index
                    } else {
                        break
                    }
                }
                val incEqValues = values.subarray(0, maxValueStartIndex + 1)
                val incEqPass = validate(SeriesType.INC_EQ, step, *incEqValues)
                if (incEqPass) {
                    val descEqValues = values.subarray(maxValueEndIndex, values.size)
                    validate(SeriesType.DESC_EQ, step, *descEqValues)
                } else {
                    false
                }
            }
            SeriesType.DESC_EQ_INC_EQ -> {
                val minValue = values.minOrNull()
                val minValueStartIndex = values.indexOf(minValue)
                if (minValueStartIndex == 0 || minValueStartIndex == values.lastIndex) {
                    return false
                }
                var minValueEndIndex = minValueStartIndex
                for (index in minValueStartIndex until values.lastIndex) {
                    if (values[index] == minValue) {
                        minValueEndIndex = index
                    } else {
                        break
                    }
                }
                val descEqValues = values.subarray(0, minValueStartIndex + 1)
                val descEqPass = validate(SeriesType.DESC_EQ, step, *descEqValues)
                if (descEqPass) {
                    val incEqValues = values.subarray(minValueEndIndex, values.size)
                    validate(SeriesType.INC_EQ, step, *incEqValues)
                } else {
                    false
                }
            }
            SeriesType.EQ -> {
                values.toSet().size == 1
            }
        }
    }
}