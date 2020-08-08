package org.kuark.base.bean.validation.support

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl
import javax.validation.ConstraintValidatorContext
import javax.validation.Validator

/**
 * Bean校验的上下文，用于传递Bean给ConstraintValidator，因为hibernate validation的ConstraintValidatorContext取不到Bean
 *
 * @author K
 * @since 1.0.0
 */
object ValidationContext {

    private val beanMap = mutableMapOf<Int, Any>() // Map<ConstraintDescriptor对象的hashcode，Bean对象>


    /**
     * 存放ConstraintDescriptor对象hashcode关联的Bean
     *
     * @param validator 验证器
     * @param bean 待校验的Bean
     */
    fun set(validator: Validator, bean: Any) {
        val beanDescriptor = validator.getConstraintsForClass(bean.javaClass)
        beanDescriptor.constrainedProperties.forEach {
            it.constraintDescriptors.forEach { des ->
                val annoClassName = des.annotation.annotationClass.qualifiedName
                if (!annoClassName!!.startsWith("javax") && !annoClassName.startsWith("org.hibernate")) {
                    beanMap[des.hashCode()] = bean
                }
            }
        }
    }

    /**
     * 获取ConstraintDescriptor对象hashcode关联的Bean，并从上下文中移除
     *
     * @param constraintValidatorContext 约束验证器上下文
     * @return 待校验的Bean
     */
    fun get(constraintValidatorContext: ConstraintValidatorContext): Any {
        val descriptor = (constraintValidatorContext as ConstraintValidatorContextImpl).constraintDescriptor
        return beanMap.remove(descriptor.hashCode())!!
    }

}