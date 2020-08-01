package org.kuark.validation.constraints

import org.kuark.validation.constraints.validator.InEnumValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 用于校验参数枚举
 *
 * @author Simon
 */
@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [InEnumValidator::class])
annotation class InEnum(
    /**
     * @return 实现 EnumValuable 接口的
     */
    val value: KClass<*> = Class::class,
    val message: String = "{InEnum.message}",  /* 值不可为空 */
    val notNull: Boolean = false,
    val groups: Array<KClass<*>> = [], // 约束注解在验证时所属的组别
    val payload: Array<KClass<out Payload>> = [] // 约束注解的有效负载
) {
    @Target(
        AnnotationTarget.ANNOTATION_CLASS,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FIELD,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.VALUE_PARAMETER
    ) // 约束注解应用的目标元素类型(METHOD, FIELD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER等)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class List(vararg val value: InEnum)
}