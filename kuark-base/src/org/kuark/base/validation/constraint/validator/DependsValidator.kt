package org.kuark.base.validation.constraint.validator

import org.kuark.base.bean.BeanKit
import org.kuark.base.validation.constraint.annotaions.Depends
import org.kuark.base.validation.support.AndOr
import org.kuark.base.validation.support.ValidationContext
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Depends约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class DependsValidator : ConstraintValidator<Depends, Any?> {

    private lateinit var depends: Depends

    override fun initialize(depends: Depends) {
        this.depends = depends
    }

    override fun isValid(value: Any?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        val bean = ValidationContext.get(constraintValidatorContext)
        return pass(depends, bean)
    }

    companion object {

        /**
         * 此方法返回值为depends里面的表达式是否成立，true为成立，false为不成立，
         * 当depends里面的表达式成立时为必填，不成立时为非必填。
         *
         * @param depends 依赖注解
         * @param bean 待校验的Bean
         * @return 是否校验通过
         */
        fun pass(depends: Depends, bean: Any): Boolean {
            val properties = depends.property
            var result = true //默认为需要验证
            properties.forEachIndexed { index, property ->
                var v1 = BeanKit.getProperty(bean, property)
                var v2 = depends.value[index]
                if (v2.startsWith("[") && v2.endsWith("]")) {
                    v2 = v2.replaceFirst("\\['".toRegex(), "")
                        .replaceFirst("'\\]".toRegex(), "")
                        .replace("',\\s*'".toRegex(), ",")
                }
                v1 = v1?.toString()
                val compare: Boolean = depends.operator[index].compare(v1, v2)
                if (depends.andOr === AndOr.AND) {
                    if (!compare) {
                        return false
                    }
                } else {
                    if (compare) {
                        return true
                    } else {
                        result = false
                    }
                }
            }
            return result
        }

    }

}