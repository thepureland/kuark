package io.kuark.base.bean.validation.constraint.validator

import io.kuark.base.bean.BeanKit
import io.kuark.base.bean.validation.constraint.annotaions.AtLeast
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * AtLeast约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class AtLeastValidator : ConstraintValidator<AtLeast, Any> {

    private lateinit var atLeast: AtLeast

    override fun initialize(atLeast: AtLeast) {
        this.atLeast = atLeast
        if (atLeast.count < 0) {
            error("@AtLeast约束指定的count值不能为负数！")
        }
        if (atLeast.count > atLeast.properties.size) {
            error("@AtLeast约束指定的count值【${atLeast.count}】不能比property的个数【${atLeast.properties.size}】大！")
        }
    }

    override fun isValid(bean: Any, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        var count = 0
        atLeast.properties.forEach { prop ->
            val value = BeanKit.getProperty(bean, prop)
            if (atLeast.logic.assert(value)) {
                count++
            }
        }
        return count >= atLeast.count
    }

}