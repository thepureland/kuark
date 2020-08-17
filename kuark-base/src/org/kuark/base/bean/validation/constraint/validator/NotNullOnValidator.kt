package org.kuark.base.bean.validation.constraint.validator

import org.kuark.base.bean.validation.constraint.annotaions.NotNullOn
import org.kuark.base.bean.validation.support.DependsValidator
import org.kuark.base.bean.validation.support.ValidationContext
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * NotNullOn约束注解验证器
 *
 * @author K
 * @since 1.0.0
 */
class NotNullOnValidator : ConstraintValidator<NotNullOn, Any?> {

    private lateinit var notNullOn: NotNullOn

    override fun initialize(notNullOn: NotNullOn) {
        this.notNullOn = notNullOn
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        val bean = ValidationContext.get(context)
        val depends = notNullOn.depends
        return if (!DependsValidator.validate(depends, bean)) { // 表达式为false，属性值可为null，即非必填选项，放行
            true
        } else value != null
    }

}