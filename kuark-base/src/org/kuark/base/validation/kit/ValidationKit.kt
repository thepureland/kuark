package org.kuark.base.validation.kit

import org.hibernate.validator.HibernateValidator
import javax.validation.ConstraintViolation
import javax.validation.Validation

/**
 * 验证工具类
 *
 * @author K
 * @since 1.0.0
 */
object ValidationKit {

    /**
     * 校验Bean
     *
     * @param bean 要校验的bean
     * @param failFast 是否为快速失败模式
     * @return Set<ConstraintViolation<Bean>>
     */
    fun <T: Any> validateBean(bean: T, failFast: Boolean = true): Set<ConstraintViolation<T>> {
        val validatorFactory = Validation.byProvider(HibernateValidator::class.java)
            .configure()
            .failFast(failFast)
            .buildValidatorFactory()
        val validator = validatorFactory.validator
        return validator.validate(bean)
    }

}