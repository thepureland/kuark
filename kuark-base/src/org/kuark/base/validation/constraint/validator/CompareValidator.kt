package org.kuark.base.validation.constraint.validator

import org.kuark.base.bean.BeanKit
import org.kuark.base.validation.constraint.annotaions.Compare
import org.kuark.base.validation.support.CompareLogic
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * 比较约束的验证器
 *
 * @author K
 * @since 1.0.0
 */
class CompareValidator : ConstraintValidator<Compare, Any> {

    private lateinit var compare: Compare

    override fun initialize(compare: Compare) {
        this.compare = compare
    }

    override fun isValid(bean: Any, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        // 依赖的前提条件不成立时，代表无须校验比较约束，直接放行
        val depends = compare.depends
        if (depends.property.isNotEmpty()) {
            if (!DependsValidator.pass(depends)) {
                return true
            }
        }

        // 比较
        val firstValue = BeanKit.getProperty(bean, compare.firstProperty)
        val secondValue = BeanKit.getProperty(bean, compare.secondProperty)
        if (firstValue::class != secondValue::class && firstValue !is Comparable<*>) {
            error("【Compare】约束注解校验的两个属性类型必须相同，且都实现【Comparablere】接口！")
        }
        return compare(firstValue as Comparable<Any>, secondValue as Comparable<Any>, compare.logic)
    }

    fun <T : Comparable<T>> compare(firstValue: T?, secondValue: T?, logic: CompareLogic): Boolean {
        if (firstValue == null && secondValue == null) {
            return true
        }
        if (firstValue == null || secondValue == null) {
            return false
        }
        return when (logic) {
            CompareLogic.EQ -> firstValue == secondValue
            CompareLogic.NE -> firstValue != secondValue
            CompareLogic.GT -> firstValue > secondValue
            CompareLogic.GE -> firstValue >= secondValue
            CompareLogic.LT -> firstValue < secondValue
            CompareLogic.LE -> firstValue <= secondValue
            CompareLogic.IEQ -> {
                if (firstValue is CharSequence || firstValue is Char) {
                    firstValue.toString().equals(secondValue.toString(), ignoreCase = true)
                } else {
                    error("操作符【CompareLogic.IEQ】只能用于类型【CharSequence或Char】的比较逻辑！")
                }
            }
        }
    }

}