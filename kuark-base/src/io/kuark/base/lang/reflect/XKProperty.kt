package io.kuark.base.lang.reflect

import kotlin.reflect.KClass
import kotlin.reflect.KProperty


/**
 * kotlin.KProperty扩展函数
 *
 * @author K
 * @since 1.0.0
 */


/**
 * 是否指定的注解类出现在该属性上
 *
 * @param annotationClass 注解类
 * @return true: 指定的注解类出现在该属性上, false: 指定的注解类没有出现在该属性上
 */
fun KProperty<*>.isAnnotationPresent(annotationClass: KClass<out Annotation>): Boolean =
    this.annotations.any { it.annotationClass == annotationClass }