package io.kuark.base.bean.validation.support

import io.kuark.base.support.logic.AndOr
import io.kuark.base.support.logic.LogicOperator

/**
 * 依赖约束注解，非一级约束注解，只作为其它一级注解的属性，代表所从属一级注解的前提条件(当Depends的条件表达式成立时，才应用该一级注解)，可参考@Compare。
 * 作为非一级约束注解，无法由validation验证框架自动调用其Validator进行校验，必须在所属一级注解的Validator中，手动调用该注解validator的对应的校验方法。
 * 如果需要实现“在其它属性满足一定条件下，才应用某个属性上的约束”，请使用@GroupSequenceProvider动态重定义默认分组。
 *
 * @author K
 * @since 1.0.0
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.EXPRESSION)
@MustBeDocumented
annotation class Depends(

    /** 依赖的属性的名称 */
    val properties: Array<String>,

    /** 表达式的逻辑操作符枚举 */
    val logics: Array<LogicOperator> = [LogicOperator.EQ],

    /**
     * 表达式的值, 必须可转为依赖属性的类型，不能申明为Array<Any>的原因是注解中都必须为常量。
     * 数组元素支持字符串形式的数组表示，如写成："[1,2,3]"。
     * 计算表达式时，左值将先计算其toString()的值，再用操作符与右值作计算。
     */
    val values: Array<String> = [],

    /** 多个表达式间的逻辑关系，默认为AND */
    val andOr: AndOr = AndOr.AND

)