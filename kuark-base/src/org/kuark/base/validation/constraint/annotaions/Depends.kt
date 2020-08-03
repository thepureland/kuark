package org.kuark.base.validation.constraint.annotaions

import org.kuark.base.validation.support.AndOr
import org.kuark.base.validation.support.Operator
import org.kuark.base.validation.constraint.validator.DependsValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 依赖约束注解（类级别），当前属性的其它约束是否应用，取决于该依赖约束是否成立
 *
 * @author K
 * @since 1.0.0
 */
@MustBeDocumented
@Constraint(validatedBy = [DependsValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Depends(

    val message: String = "{Depends.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],

    /**
     * 依赖的属性的名称
     *
     * @return 依赖的属性的名称
     */
    val property: Array<String>,
    /**
     * 表达式的操作符枚举
     *
     * @return 表达式的操作符枚举
     */
    val operator: Array<Operator> = [Operator.IS_NOT_NULL],
    /**
     * 多个表达式间的逻辑关系，默认为AND.
     */
    val andOr: AndOr = AndOr.AND,
    /**
     * 获取javascript值的表达式，缺省为：$('[name="propertyName"]').val()
     * @return 获取javascript值的表达式
     */
    val jsValueExp: Array<String> = [],
    /**
     * 表达式的值, 必须可转为依赖属性的类型
     *
     * @return 表达式的值, 必须可转为依赖属性的类型
     */
    vararg val value: String = [""]
) {
    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
    )
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class List(vararg val value: Depends)
}