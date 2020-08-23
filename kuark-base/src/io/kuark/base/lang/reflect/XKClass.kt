package io.kuark.base.lang.reflect

import java.net.URLDecoder
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.allSuperclasses
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
 * 返回当前类实现的所有接口
 *
 * @return 所有接口
 */
fun KClass<*>.getAllInterfaces(): List<KClass<*>> = this.allSuperclasses.filter { it.constructors.isEmpty() }

/**
 * 匹配第一个与代表当前类的Type
 *
 * @param 待搜索的type集合
 * @return 第一个与代表当前类的Type
 */
fun KClass<*>.firstMatchTypeOf(types: Collection<KType>): KType = types.first { it.classifier == this }

/**
 * 返回在指定类的类体系(向上)中，匹配类注解的类
 *
 * @param annoClass 注解类
 * @return 匹配的类
 */
fun KClass<*>.getClassUpThatPresentAnnotation(annoClass: KClass<out Annotation>): KClass<*> {
    val present = this.isAnnotationPresent(annoClass)
    return if (present) {
        this
    } else {
        this.getSuperClass().getClassUpThatPresentAnnotation(annoClass)
    }
}

/**
 * 获取类在磁盘上的物理位置
 *
 * @return 类文件的绝对路径
 * @since 1.0.0
 */
fun KClass<*>.getLocationOnDisk(): String =
    URLDecoder.decode(this.java.protectionDomain.codeSource.location.path, "UTF-8")