package org.kuark.base.validation.kit

import org.hibernate.validator.HibernateValidator
import org.kuark.base.validation.support.ValidationContext
import javax.validation.ConstraintViolation
import javax.validation.Validation
import kotlin.reflect.KClass

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
     * @param groups 标识分组的Class数组，不为空将只校验指定分组的约束
     * @param failFast 是否为快速失败模式
     * @return Set<ConstraintViolation<Bean>>
     */
    fun <T : Any> validate(
        bean: T,
        vararg groups: KClass<*> = arrayOf(),
        failFast: Boolean = true
    ): Set<ConstraintViolation<T>> {
        val validatorFactory = Validation.byProvider(HibernateValidator::class.java)
            .configure()
            .failFast(failFast)
            .buildValidatorFactory()
        ValidationContext.set(validatorFactory, bean)
        val classes = groups.map { it.java }.toTypedArray()
        return validatorFactory.validator.validate(bean, *classes)
    }


}