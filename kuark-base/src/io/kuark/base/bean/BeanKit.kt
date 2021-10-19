package io.kuark.base.bean

import io.kuark.base.lang.SerializationKit
import io.kuark.base.lang.collections.MapKit
import io.kuark.base.lang.reflect.getEmptyConstructor
import io.kuark.base.support.Consts
import io.kuark.base.support.IIdEntity
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.beanutils.PropertyUtils
import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.io.Serializable
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * Bean操作工具类
 *
 * @author K
 * @since 1.0.0
 */
object BeanKit {

    /**
     * 深度克隆指定的bean
     * 该方法比直接在对象图中的所有对象重写克隆方法慢很多倍. 但是, 对于复杂的对象图, 或那些不支持深度克隆的对象, 这提供了另一种实现. 当然, 所有对象都必须实现 `Serializable`接口.
     *
     * @param T bean类型
     * @param bean 被克隆的bean
     * @return 克隆后的bean
     * @throws org.apache.commons.lang3.SerializationException (运行时) 如果序列化失败
     * @see SerializationKit.clone
     * @author K
     * @since 1.0.0
     */
    fun <T : Serializable> deepClone(bean: T): T = SerializationKit.clone(bean)

    /**
     * 根据字段映射，拷贝源对象的属性，到指定目标类对象的对应属性
     *
     * @param T 目标类型
     * @param destClass 目标类
     * @param srcObj 源对象
     * @param propertyMap 字段映射 Map(源对象属性名，目标对象属性名)，为null或空将尝试拷贝所有源对象的属性到目标对象的对应属性(如果存在的话)
     * @return 目标类的对象
     * @author K
     * @since 1.0.0
     */
    fun <T : Any> copyProperties(destClass: KClass<T>, srcObj: Any, propertyMap: Map<String, String>? = null): T {
        val destObj = destClass.getEmptyConstructor()!!.call()
        copyProperties(srcObj, destObj, propertyMap)
        return destObj
    }

    /**
     * 根据字段映射，拷贝源对象的属性，到指定目标对象的对应属性
     *
     * @param T 目标类型
     * @param srcObj 源对象
     * @param destObj 目标对象
     * @param propertyMap 字段映射 Map(源对象属性名，目标对象属性名)，为null或空将尝试拷贝所有源对象的属性到目标对象的对应属性(如果存在的话)
     * @return 目标类的对象
     * @author K
     * @since 1.0.0
     */
    fun <T : Any> copyProperties(srcObj: Any, destObj: T, propertyMap: Map<String, String>? = null): T {
        val map = if (MapKit.isEmpty(propertyMap)) { // 将拷贝所有源对象的属性
            val mappingRule = mutableMapOf<String, String>()
            srcObj::class.memberProperties.forEach {
                mappingRule[it.name] = it.name
            }
            mappingRule
        } else propertyMap

        for ((srcPropertyName, destPropertyName) in map!!) {
            if (srcPropertyName.isNotBlank() && destPropertyName.isNotBlank()) {
                val result = getProperty(srcObj, srcPropertyName)
                setProperty(destObj, destPropertyName, result)
            }
        }
        return destObj
    }

    /**
     * 拷贝除了主键外的所有其它属性
     *
     * @param T 实体对象类型
     * @param src 源对象
     * @param dest 目标对象
     * @return 目标类的对象
     * @throws java.lang.reflect.InvocationTargetException 目标调用时发生异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @author K
     * @since 1.0.0
     */
    fun <T> copyPropertiesExcludeId(src: IIdEntity<T>, dest: IIdEntity<T>): IIdEntity<T> {
        val id = dest.id
        copyProperties(src, dest, null)
        dest.id = id
        return dest
    }

    /**
     * 拷贝对象，排除指定的属性(不支持嵌套/索引/映射/组合)
     *
     * @param T 目标类型
     * @param source 源对象
     * @param target 目标对象
     * @param excludeProperties 不拷贝的属性可变数组
     * @throws java.lang.reflect.InvocationTargetException 目标调用时发生异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @throws NoSuchMethodException 如果找不到指定的可访问的方法
     * @throws java.beans.IntrospectionException 内省异常
     * @author K
     * @since 1.0.0
     */
    fun <T : Any> copyPropertiesExclude(source: Any, target: T, vararg excludeProperties: String): T {
        val beanInfo = Introspector.getBeanInfo(target.javaClass)
        val targetPds = beanInfo.propertyDescriptors
        for (targetPd in targetPds) {
            if (targetPd.writeMethod != null && targetPd.name !in excludeProperties) {
                val sourcePd: PropertyDescriptor = PropertyUtils.getPropertyDescriptor(source, targetPd.name)
                sourcePd.readMethod?.run {
                    if (!Modifier.isPublic(declaringClass.modifiers)) {
                        isAccessible = true
                    }
                    val value = invoke(source)
                    val writeMethod = targetPd.writeMethod
                    if (!Modifier.isPublic(writeMethod.declaringClass.modifiers)) {
                        writeMethod.isAccessible = true
                    }
                    writeMethod.invoke(target, value)
                }
            }
        }
        return target
    }

    /**
     * 重置所有的非id属性的值
     *
     * @param T 实体类型
     * @param entity 目标实体bean
     * @throws java.lang.reflect.InvocationTargetException 目标调用时发生异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @throws InstantiationException 实例化异常
     * @author K
     * @since 1.0.0
     */
    fun <T> resetPropertiesExcludeId(entity: IIdEntity<T>) {
        val id = entity.id
        val emptyEntity: IIdEntity<T> = entity::class.getEmptyConstructor()!!.call()
        copyProperties(emptyEntity, entity, null)
        entity.id = id
    }

    /**
     * 批量属性拷贝
     *
     * @param T 目标类型
     * @param targetClass 目标类
     * @param srcObjs 源对象集合
     * @return List(目标类对象)
     * @author K
     * @since 1.0.0
     */
    fun <T : Any> batchCopyProperties(targetClass: KClass<T>, srcObjs: Collection<Any>): List<T> =
        srcObjs.map { copyProperties(targetClass, it) }

    //region 封装org.apache.commons.beanutils.BeanUtils和PropertyUtils
    /**
     * 基于可用的属性的getters和setters克隆(浅克隆)一个bean，即使该bean本身未实现Cloneable接口
     *
     * @param T bean类型
     * @param bean 被克隆的bean
     * @return 克隆后的bean
     * @throws java.lang.reflect.InvocationTargetException 目标调用时发生异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @throws NoSuchMethodException 如果找不到指定的可访问的方法
     * @throws InstantiationException 实例化异常
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    fun <T> shallowClone(bean: T): T = BeanUtils.cloneBean(bean) as T

//    /**
//     * 拷贝(浅克隆)所有源bean的属性值到目标bean相同的属性值，能进行类型转换
//     *
//     * @param orig 源bean
//     * @param dest 目标bean
//     * @param T 目标类型
//     * @return 目标对象
//     * @throws java.lang.reflect.InvocationTargetException 目标调用时发生异常
//     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
//     * @author K
//     * @since 1.0.0
//     */
//    fun <T> copyProperties(orig: Any?, dest: T): T {
//        BeanUtils.copyProperties(orig, dest)
////        org.springframework.beans.BeanUtils.copyProperties(orig, dest)
////        return dest
//        TODO()
//    }

    /**
     * 拷贝(浅克隆)所有源bean的属性值到目标bean相同的属性值，不能进行类型转换
     *
     * @param T 目标bean类型
     * @param orig 源bean
     * @param dest 目标bean
     * @throws java.lang.reflect.InvocationTargetException 目标调用时发生异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @throws NoSuchMethodException 如果找不到指定的可访问的方法
     * @author K
     * @since 1.0.0
     */
    fun <T : Any> copyProperties(orig: Any, dest: T) = PropertyUtils.copyProperties(dest, orig)

    /**
     * 返回指定bean的所有属性名及其值
     *
     * @param bean 被提取属性的bean
     * @return Map(属性名，属性值)
     * @throws java.lang.reflect.InvocationTargetException 目标调用时发生异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @throws NoSuchMethodException 如果找不到指定的可访问的方法
     * @author K
     * @since 1.0.0
     */
    fun extract(bean: Any): Map<String, Any?> = PropertyUtils.describe(bean)

    /**
     * 返回指定属性的值
     *
     * @param bean 目标bean
     * @param name 属性名(可以嵌套/索引/映射/组合)
     * @return 属性值
     * @throws java.lang.reflect.InvocationTargetException 目标调用时发生异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @throws NoSuchMethodException 如果找不到指定的可访问的方法
     * @author K
     * @since 1.0.0
     */
    fun getProperty(bean: Any, name: String): Any? = PropertyUtils.getProperty(bean, name)

    /**
     * 设置属性值(浅克隆), 能进行类型转换
     *
     * @param T bean类型
     * @param bean 目标bean
     * @param name 属性名(可以嵌套/索引/映射/组合)
     * @param value 属性值
     * @return 目标bean
     * @throws java.lang.reflect.InvocationTargetException 目标调用时发生异常
     * @throws IllegalAccessException
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    fun <T> setProperty(bean: T, name: String?, value: Any?): T = BeanUtils.copyProperty(bean, name, value) as T

    //endregion 封装org.apache.commons.beanutils.BeanUtils和PropertyUtils

}