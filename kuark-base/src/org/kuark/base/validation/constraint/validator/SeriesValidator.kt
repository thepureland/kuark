package org.kuark.base.validation.constraint.validator

import org.kuark.base.validation.constraint.annotaions.Series
import org.kuark.base.validation.support.CompareLogic
import org.kuark.base.validation.support.SeriesType
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * @author admin
 * @time 9/19/15 6:50 PM
 */
class SeriesValidator : ConstraintValidator<Series, Any?> {

    private lateinit var series: Series

    override fun initialize(series: Series) {
        this.series = series
    }

    override fun isValid(o: Any?, context: ConstraintValidatorContext): Boolean {
        if (o == null) {
            return true
        }
        if (o is Array<*>) {
            val values = o as Array<Any?>
            if (values.size <= 1) {
                return true
            }
            //            Object firstValue = values[0];
//            Class<?> elemClass = null;
//            if (firstValue == null) {
//                throw new SystemException("@Series表单验证规则限制数组中每个元素均不能为null！数组为：" + values);
//            } else {
//                elemClass = firstValue.getClass();
//            }
            val logic = adaptCompareLogic(series!!.type)
            var preValue: Any? = null
            for (value in values) {
                if (preValue != null) {
                    if (value == null) {
                        throw Exception("@Series表单验证规则限制数组中每个元素均不能为null！数组为：$values")
                    }
//                    val compare: Boolean = CompareValidator.compare(value, preValue, logic)
//                    if (!compare) {
//                        return false
//                    }
                }
                preValue = value
            }
        } else {
            throw Exception("@Series表单验证规则只能设置在返回值为数组的get方法上！")
        }
        return true
    }

    private fun adaptCompareLogic(seriesType: SeriesType): CompareLogic {
        return when (seriesType) {
            SeriesType.INC -> CompareLogic.GT
            SeriesType.DESC -> CompareLogic.LT
            SeriesType.INC_EQ -> CompareLogic.GE
            SeriesType.DESC_EQ -> CompareLogic.LE
            SeriesType.DIFF -> CompareLogic.NE
        }
        throw Exception("不支持的SeriesType类型：$seriesType")
    }
}