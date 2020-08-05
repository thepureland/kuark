package org.kuark.base.validation.constraint.annotaions

import org.kuark.base.validation.constraint.validator.DependsValidator
import org.kuark.base.validation.support.AndOr
import org.kuark.base.validation.support.Group
import org.kuark.base.validation.support.Operator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 依赖约束注解，当前属性的其它约束是否应用，取决于该依赖约束表达式是否成立。
 *
 * 使用该注解必须指定分组，然后用@GroupSequence保证@Depends的优先级高于当前属性的其它约束注解的优先级。
 * @GroupSequence使用注意事项：
 * 1.不能包含javax.validation.groups.Default::class分组
 * 2.不能没有待验证的Bean的Class的分组
 *
 * @author K
 * @since 1.0.0
 */
@Constraint(validatedBy = [DependsValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Depends(

    /** 依赖的属性的名称 */
    val property: Array<String>,

    /** 表达式的操作符枚举 */
    val operator: Array<Operator> = [Operator.IS_NOT_NULL],

    /** 多个表达式间的逻辑关系，默认为AND */
    val andOr: AndOr = AndOr.AND,

    /**
     * 表达式的值, 必须可转为依赖属性的类型，不能申明为Array<Any>的原因是注解中都必须为常量。
     * 数组元素支持字符串形式的数组表示，如写成："[1,2,3]"。
     * 计算表达式时，左值将先计算其toString()的值，再用操作符与右值作计算。
     */
    val value: Array<String> = [""],

    /** 获取javascript值的表达式，缺省为：$('[name="propertyName"]').val() */
    val jsValueExp: Array<String> = [],


    /** 校验不通过时的提示，或其国际化key */
    val message: String = "",

    /** 该校验规则所从属的分组类，通过分组可以过滤校验规则或排序校验顺序。默认值必须是空数组。 */
    val groups: Array<KClass<*>> = [],

    /** 约束注解的有效负载(通常用来将一些元数据信息与该约束注解相关联，常用的一种情况是用负载表示验证结果的严重程度) */
    val payload: Array<KClass<out Payload>> = []

) {

    /**
     * 多个Depends约束
     * 使用该约束注解时，Bean Validation 将 Compare 数组里面的每一个元素都处理为一个普通的约束注解，并对其进行验证，所有约束条件均符合时才会验证通过
     */
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class List(vararg val value: Depends)

}