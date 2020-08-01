package org.kuark.base.validation.annotaions

import org.kuark.base.validation.support.AndOr
import org.kuark.base.validation.support.Operator
import org.kuark.base.validation.validator.DependsValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 表单验证条件(依赖)规则注解，该注解表达式成立时，其对应的属性的其他注解才能生效
 *
 * @author admin
 * @time 8/12/15 5:28 PM
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