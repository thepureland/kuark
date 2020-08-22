package io.kuark.base.lang.reflect

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.memberFunctions
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
 * 返回给定名称的成员函数对象
 *
 * @param functionName 成员函数名
 * @return 成员函数对象
 * @throws NoSuchElementException 当不存在时
 */
fun KClass<*>.getMemberFunction(functionName: String): KFunction<*> =
    this.memberFunctions.first{ it.name == functionName }


/**
 * 返回当前类的直接父类
 *
 * @return 直接父类
 * @throws NoSuchElementException 当不存在时
 */
fun KClass<*>.getSuperClass(): KClass<*> = this.superclasses.first { it.constructors.isNotEmpty() }

/**
 * 返回当前类的直接父接口
 *
 * @return 直接父接口列表
 */
fun KClass<*>.getSuperInterfaces(): List<KClass<*>> = this.superclasses.filter { it.constructors.isEmpty() }

/**
 * 匹配第一个与代表当前类的Type
 *
 * @return 第一个与代表当前类的Type
 */
fun KClass<*>.firstMatchTypeOf(types: Collection<KType>): KType = types.first { it.classifier == this }