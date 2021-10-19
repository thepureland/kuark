package io.kuark.base.lang.reflect

import java.net.URLDecoder
import kotlin.reflect.*
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
 * 返回空构造器，如果没有返回null
 *
 * @param T 当前类型
 * @return 空构造器
 * @author K
 * @since 1.0.0
 */
fun <T : Any> KClass<T>.getEmptyConstructor(): KFunction<T>? {
    val constructors = this.constructors.filter { it.parameters.isEmpty() }
    return if (constructors.isEmpty()) {
        null
    } else {
        constructors.first()
    }
}

/**
 * 实例化类
 *
 * @param T 当前类型
 * @param args 构造函数参数可变数组
 * @return 类对象
 * @author K
 * @since 1.0.0
 */
fun <T : Any> KClass<T>.newInstance(vararg args: Any): T {
    if (this.isAbstract) {
        error("抽象的类${this}不能被实例化！")
    }
    if (this.isCompanion) {
        error("Companion类${this}不能被实例化！")
    }
    this.constructors.forEach {
        val parameters = it.parameters
        if (parameters.size == args.size) {
            var match = true
            for (index in parameters.indices) {
                if (args[index]::class != parameters[index].type.classifier) {
                    match = false
                    break
                }
            }
            if (match) {
                return it.call(*args)
            }
        }
    }
    error("实例化${this}时，未能找到匹配参数的构造函数！")
}

/**
 * 当前类是否为枚举
 *
 * @return true: 当前类是为枚举，false：当前类不是枚举
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.isEnum(): Boolean = this.getSuperClass() == Enum::class

/**
 * 当前类是否为接口
 *
 * @return true: 当前类是为接口，false：当前类不是接口
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.isInterface(): Boolean = this.isAbstract && this.constructors.isEmpty()

/**
 * 当前类是否为抽象类
 *
 * @return true: 当前类是为抽象类，false：当前类不是抽象类
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.isAbstractClass(): Boolean = this.isAbstract && this.constructors.isNotEmpty()

/**
 * 当前类是否为注解
 *
 * @return true: 当前类是为注解，false：当前类不是注解
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.isAnnotation(): Boolean =
    this.isFinal && this.getSuperInterfaces().contains(Annotation::class) && getSuperClass() == Any::class

/**
 * 是否指定的注解类出现在该类上。
 * 注意：对于像SinceKotlin的注解无效!
 *
 * @param annotationClass 注解类
 * @return true: 指定的注解类出现在该类上, false: 指定的注解类没有出现在该类上
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.isAnnotationPresent(annotationClass: KClass<out Annotation>): Boolean =
    this.annotations.any { it.annotationClass == annotationClass }

/**
 * 返回给定属性名的属性对象
 *
 * @param T 当前类型
 * @param propertyName 属性名
 * @return 属性对象
 * @throws NoSuchElementException 当不存在时
 * @author K
 * @since 1.0.0
 */
fun <T : Any> KClass<T>.getMemberProperty(propertyName: String): KProperty1<T, Any?> =
    this.memberProperties.first { it.name == propertyName }

/**
 * 返回属性值
 *
 * @param target 目标对象
 * @param propertyName 属性名
 * @return 属性的值
 * @throws NoSuchElementException 当不存在时
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.getMemberPropertyValue(target: Any, propertyName: String): Any? {
    val memberProperty = this.getMemberProperty(propertyName)
    return memberProperty.call(target)
}

/**
 * 返回给定名称和参数的成员函数对象
 *
 * @param functionName 成员函数名
 * @param parameters 函数参数可变数组
 * @return 成员函数对象
 * @throws NoSuchElementException 当不存在时
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.getMemberFunction(functionName: String, vararg parameters: KParameter): KFunction<*> {
    val functions = this.memberFunctions.filter { it.name == functionName }
    if (functions.isEmpty()) {
        throw NoSuchElementException("类【${this}】中找不到命名为【${functionName}】的方法！")
    } else if (functions.size == 1) {
        return functions.first()
    } else {
        functions.forEach {
            var match = true
            for (index in it.parameters.indices) {
                if (it.parameters[index].type != parameters[index].type) {
                    match = false
                    break
                }
            }
            if (match) {
                return it
            }
        }
        throw NoSuchElementException("类【${this}】中找不到命名为【${functionName}】且匹配参数类型的方法！")
    }
}

/**
 * 返回当前类的直接父类
 *
 * @return 直接父类，当前类为Any时返回null
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.getSuperClass(): KClass<*>? {
    if (this == Any::class) return null
    return superclasses.first { it.constructors.isNotEmpty() }
}

/**
 * 返回当前类的直接父接口
 *
 * @return 直接父接口列表
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.getSuperInterfaces(): List<KClass<*>> = this.superclasses.filter { it.constructors.isEmpty() }

/**
 * 返回当前类实现的所有接口
 *
 * @return 所有接口
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.getAllInterfaces(): List<KClass<*>> = this.allSuperclasses.filter { it.constructors.isEmpty() }

/**
 * 匹配第一个与代表当前类的Type
 *
 * @param types 待搜索的type集合
 * @return 第一个与代表当前类的Type
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.firstMatchTypeOf(types: Collection<KType>): KType = types.first { it.classifier == this }

/**
 * 返回在指定类的类体系(向上)中，匹配类注解的类
 * 注意：对于像SinceKotlin的注解无效!
 *
 * @param annoClass 注解类
 * @return 匹配的类的Set
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.getClassUpThatPresentAnnotation(annoClass: KClass<out Annotation>): Set<KClass<*>> {
    val results = mutableSetOf<KClass<*>>()
    getClassUpThatPresentAnnotation(this, annoClass, results)
    return results
}

private fun getClassUpThatPresentAnnotation(
    clazz: KClass<*>, annoClass: KClass<out Annotation>, results: MutableSet<KClass<*>>
) {
    if (clazz == Any::class) {
        return
    }
    val present = clazz.isAnnotationPresent(annoClass)
    if (present) {
        results.add(clazz)
    }
    clazz.allSuperclasses.forEach { getClassUpThatPresentAnnotation(it, annoClass, results) }
}

/**
 * 获取类在磁盘上的物理位置
 *
 * @return 类文件的绝对路径
 * @since 1.0.0
 * @author K
 * @since 1.0.0
 */
fun KClass<*>.getLocationOnDisk(): String =
    URLDecoder.decode(this.java.protectionDomain.codeSource.location.path, "UTF-8")