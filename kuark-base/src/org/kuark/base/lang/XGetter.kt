package org.kuark.base.lang

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * kotlin.KProperty.Getter扩展函数
 *
 * @author K
 * @since 1.0.0
 */


/**
 * 是否指定的注解类出现在该Getter上
 *
 * @param annotationClass 注解类
 * @return true: 指定的注解类出现在该Getter上, false: 指定的注解类没有出现在该Getter上
 */
fun KProperty.Getter<*>.isAnnotationPresent(annotationClass: KClass<out Annotation>): Boolean =
    this.annotations.any { it.annotationClass == annotationClass }