package org.kuark.base.validation.constraint.validator

import org.kuark.base.validation.constraint.annotaions.Series
import org.kuark.base.validation.support.SeriesType
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
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        if (value is Array<*>) {
            if (value.size <= 1) {
                return true
            }
            value.forEach {
                it ?: error("@Series约束注解限制数组中每个元素均不能为null！数组为：$value")
            }

            // 将数组元素全部转为String，方便用BigDecimal进行高精度运算
            val values = value.map { it.toString() }.toTypedArray()
            return validate(series.type, series.step, *values)
        } else {
            error("@Series约束注解只能设置在返回值类型为数组的get方法上！")
        }
        return true
    }

    private fun validate(type: SeriesType, step: Double, vararg values: String): Boolean {
        return when (type) {
            SeriesType.INC_DIFF -> {
                var preValue: String? = null
                for (value in values) {
                    if (preValue != null) {
                        if (step == 0.0) { // 不应用步进
                            if (BigDecimal(preValue) >= BigDecimal(value)) {
                                return false
                            }
                        } else {
                            if (BigDecimal(preValue) + BigDecimal(step) != BigDecimal(value)) {
                                return false
                            }
                        }
                    }
                    preValue = value
                }
                true
            }
            SeriesType.DESC_DIFF -> true
            SeriesType.INC_DESC_DIFF -> true
            SeriesType.DESC_INC_DIFF -> true
            SeriesType.DIFF -> true
            SeriesType.INC_EQ -> true
            SeriesType.DESC_EQ -> true
            SeriesType.INC_EQ_DESC_EQ -> true
            SeriesType.DESC_EQ_INC_EQ -> true
            SeriesType.EQ -> true
        }
    }
}