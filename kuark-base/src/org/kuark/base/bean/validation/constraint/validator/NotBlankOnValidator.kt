package org.kuark.base.bean.validation.constraint.validator

import org.kuark.base.bean.validation.constraint.annotaions.NotBlankOn
import org.kuark.base.bean.validation.support.DependsValidator
import org.kuark.base.bean.validation.support.ValidationContext
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * NotBlankOn约束注解验证器
 *
 * @author K
 * @since 1.0.0
 */
class NotBlankOnValidator : ConstraintValidator<NotBlankOn, Any?> {

    private lateinit var notBlankOn: NotBlankOn

    override fun initialize(notBlankOn: NotBlankOn) {
        this.notBlankOn = notBlankOn
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        val bean = ValidationContext.get(context)
        val depends = notBlankOn.depends
        return if (!DependsValidator.validate(depends, bean)) { // 表达式为false，属性值可为空白，放行
            true
        } else {
            //TODO
            value != null
        }
    }

}