package io.kuark.base.lang.reflect

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.superclasses

/**
 * kotlin.KClass扩展函数
 *
 * @author K
 * @since 1.0.0
 */


/**
 * 是否指定的注解类出现在该类上
 *
 * @param annotationClass 注解类
 * @return true: 指定的注解类出现在该类上, false: 指定的注解类没有出现在该类上
 */
fun KClass<*>.isAnnotationPresent(annotationClass: KClass<out Annotation>): Boolean =
    this.annotations.any { it.annotationClass == annotationClass }

/**
 * 返回给定属性名的属性对象
 *
 * @param propertyName 属性名
 * @return 属性对象
 * @throws NoSuchElementException 当不存在时
 */
fun KClass<*>.getMemberProperty(propertyName: String): KProperty<*> =
    this.memberProperties.first { it.name == propertyName }

/**
 * 返回属性值
 *
 * @param target 目标对象
 * @param propertyName 属性名
 * @return 属性的值
 * @throws NoSuchElementException 当不存在时
 */
fun KClass<*>.getMemberPropertyValue(target: Any, propertyName: String): Any? {
    val memberProperty = this.getMemberProperty(propertyName)
    return memberProperty.call(target)
}


/**
 * 返回当前类的直接父类
 *
 * @return 直接父类
 * @throws NoSuchElementException 当不存在时
 */
fun KClass<*>.getDirectSuperClass(): KClass<*> = this.superclasses.first { it.constructors.isNotEmpty() }