package org.kuark.validation.constraints

import org.kuark.validation.constraints.impl.DependsValidator
import org.kuark.validation.constraints.support.AndOr
import org.kuark.validation.constraints.support.Operator
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 表单验证条件(依赖)规则注解，该注解表达式成立时，其对应的属性的其他注解才能生效
 *
 * @author admin
 * @time 8/12/15 5:28 PM
 */
@Documented
@Constraint(validatedBy = [DependsValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
annotation class Depends(

    val message: String = "{org.kuark.validation.constraints.Depends.message}",
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
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    annotation class List(vararg val value: Depends)
}