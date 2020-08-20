package io.kuark.base.bean

import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.beanutils.ConvertUtils
import org.apache.commons.beanutils.PropertyUtils
import org.apache.commons.beanutils.converters.DateConverter
import io.kuark.base.lang.SerializationKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.IIdEntity
import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.io.Serializable
import java.lang.reflect.Modifier
import java.util.*
import kotlin.reflect.KClass

/**
 * Bean操作工具类
 *
 * @author K
 * @since 1.0.0
 */
object BeanKit {

    private val LOG = LogFactory.getLog(BeanKit::class)

    /**
     * 深度克隆指定的bean
     * 该方法比直接在对象图中的所有对象重写克隆方法慢很多倍. 但是, 对于复杂的对象图, 或那些不支持深度克隆的对象, 这提供了另一种实现. 当然, 所有对象都必须实现 `Serializable`接口.
     *
     * @param bean 被克隆的bean
     * @return 克隆后的bean
     * @throws SerializationException (运行时) 如果序列化失败
     * @see SerializationKit.clone
     * @since 1.0.0
     */
    fun <T : Serializable?> deepClone(bean: T): T = SerializationKit.clone(bean)

    /**
     * 根据字段映射，拷贝源对象的属性，到指定目标类对象的对应属性
     *
     * @param destClass 目标类
     * @param srcObj 源对象
     * @param propertyMap 字段映射 Map<源对象属性名，目标对象属性名>
     * @return 目标类的对象
     * @since 1.0.0
     */
    fun <T: Any> copyProperties(destClass: KClass<T>, srcObj: Any, propertyMap: Map<String, String>): T? {
        var destObj: T? = null
        try {
            destObj = destClass.java.getDeclaredConstructor().newInstance()
            copyProperties(destObj, srcObj, propertyMap)
        } catch (e: Exception) {
            val msgPattern = "属性拷贝失败! destClass：{0}，srcObj：{1}， fieldMap: {2}"
            LOG.error(e, msgPattern, destClass, srcObj, propertyMap)
        }
        return destObj
    }

    /**
     * 根据字段映射，拷贝源对象的属性，到指定目标对象的对应属性
     *
     * @param destObj 目标对象
     * @param srcObj 源对象
     * @param propertyMap 字段映射 Map<源对象属性名，目标对象属性名>
     * @return 目标类的对象
     * @since 1.0.0
     */
    fun <T> copyProperties(destObj: T, srcObj: Any, propertyMap: Map<String, String>): T {
        try {
            val entrySet = propertyMap.entries
            for ((srcField, destFieldStr) in entrySet) {
                if (srcField.isNotBlank() && destFieldStr.isNotBlank()) {
                    val result = getProperty(srcObj, srcField)
                    copyProperty(destObj, destFieldStr, result)
                }
            }
        } catch (e: Exception) {
            val msgPattern = "属性拷贝失败! destObj：{0}，srcObj：{1}， fieldMap: {2}"
            LOG.error(e, msgPattern, destObj, srcObj, propertyMap)
        }
        return destObj
    }

    /**
     * 拷贝源对象的所有属性到指定类的对象的对应属性
     *
     * @param srcObj 源对象
     * @param targetClass 目标类
     * @return 目标类的对象
     * @InvocationTargetException 对被调用方法的包装异常
     * @IllegalAccessException 如果请求的方法不能通过反射访问
     * InstantiationException 实例化异常
     * @since 1.0.0
     */
    fun <T: Any> copyProperties(srcObj: Any, targetClass: KClass<T>): T {
        val target = targetClass.java.getDeclaredConstructor().newInstance()
        return copyProperties(srcObj, target)
    }

    /**
     * 拷贝除了主键外的属性
     *
     *  @param src 源对象
     * @param dest 目标对象
     * @return 目标类的对象
     * @InvocationTargetException 对被调用方法的包装异常
     * @IllegalAccessException 如果请求的方法不能通过反射访问
     * @since 1.0.0
     */
    fun <T> copyPropertiesExcludeId(src: IIdEntity<T>, dest: IIdEntity<T>): IIdEntity<T> {
        val id = dest.id
        copyProperties(src, dest)
        dest.id = id
        return dest
    }

    /**
     * 拷贝对象，排除指定的属性(不支持嵌套/索引/映射/组合)
     *
     * @param source 源对象
     * @param target 目标对象
     * @param excludeProperties 不拷贝的属性可变数组
     * @InvocationTargetException 对被调用方法的包装异常
     * @IllegalAccessException 如果请求的方法不能通过反射访问
     * @NoSuchMethodException 如果找不到指定的可访问的方法
     * IntrospectionException 内省异常
     * @since 1.0.0
     */
    fun <T:Any> copyPropertiesExclude(source: Any?, target: T, vararg excludeProperties: String?): T {
        val beanInfo = Introspector.getBeanInfo(target.javaClass)
        val targetPds = beanInfo.propertyDescriptors
        val ignoreList = listOf(*excludeProperties)
        for (targetPd in targetPds) {
            if (targetPd.writeMethod != null && !ignoreList!!.contains(targetPd.name)) {
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
     * @param entity 目标bean
     * @InvocationTargetException 对被调用方法的包装异常
     * @IllegalAccessException 如果请求的方法不能通过反射访问
     * InstantiationException 实例化异常
     * @since 1.0.0
     */
    fun <T> resetPropertiesExcludeId(entity: IIdEntity<T>) {
        val id = entity.id
        val emptyEntity: IIdEntity<T> = entity.javaClass.getDeclaredConstructor().newInstance()
        copyProperties(emptyEntity, entity)
        entity.id = id
    }

    /**
     * 批量属性拷贝
     *
     * @param targetClass 目标类
     * @param srcObjs 源对象集合
     * @return List<目标类对象>
     * @since 1.0.0
     */
    fun <T: Any> batchCopyProperties(targetClass: KClass<T>, srcObjs: Collection<*>): List<T> {
        val targetList = mutableListOf<T>()
        for (srcObj in srcObjs) {
            targetList.add(BeanKit.copyProperties(srcObj!!, targetClass))
        }
        return targetList
    }

    //region 封装org.apache.commons.beanutils.BeanUtils和PropertyUtils
    /**
     * 基于可用的属性的getters和setters克隆(浅克隆)一个bean，即使该bean本身未实现Cloneable接口
     *
     * @param bean 被克隆的bean
     * @return 克隆后的bean
     * @InvocationTargetException 对被调用方法的包装异常
     * @IllegalAccessException 如果请求的方法不能通过反射访问
     * @NoSuchMethodException 如果找不到指定的可访问的方法
     * InstantiationException 实例化异常
     * @since 1.0.0
     */
    fun <T> shallowClone(bean: T): T = BeanUtils.cloneBean(bean) as T

//    /**
//     * 拷贝(浅克隆)所有源bean的属性值到目标bean相同的属性值，能进行类型转换
//     *
//     * @param orig 源bean
//     * @param dest 目标bean
//     * @return 目标对象
//     * @InvocationTargetException 对被调用方法的包装异常
//     * @IllegalAccessException 如果请求的方法不能通过反射访问
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
     * @param orig 源bean
     * @param dest 目标bean
     * @return 目标对象
     * @InvocationTargetException 对被调用方法的包装异常
     * @IllegalAccessException 如果请求的方法不能通过反射访问
     * @NoSuchMethodException 如果找不到指定的可访问的方法
     * @since 1.0.0
     */
    fun <T> copyProperties(orig: Any, dest: T): T = PropertyUtils.copyProperties(dest, orig) as T

    /**
     * 拷贝(浅克隆)一个指定的属性值到指定的目标bean, 能进行类型转换
     *
     * @param bean 目标bean
     * @param name 属性名(可以嵌套/索引/映射/组合)
     * @param value 属性值
     * @return 目标bean
     * @InvocationTargetException 对被调用方法的包装异常
     * @IllegalAccessException 如果请求的方法不能通过反射访问
     * @since 1.0.0
     */
    fun <T> copyProperty(bean: T, name: String?, value: Any?): T = BeanUtils.copyProperty(bean, name, value) as T

    /**
     * 返回指定bean的所有提供getter的属性及其值的Map
     *
     * @param bean 被提取属性的bean
     * @return Map<属性名，属性值>
     * @InvocationTargetException 对被调用方法的包装异常
     * @IllegalAccessException 如果请求的方法不能通过反射访问
     * @NoSuchMethodException 如果找不到指定的可访问的方法
     * @since 1.0.0
     */
    fun extract(bean: Any): Map<String, Any?> = PropertyUtils.describe(bean)

    /**
     * 返回指定属性的值
     *
     * @param bean 目标bean
     * @param name 属性名(可以嵌套/索引/映射/组合)
     * @return 属性值
     * @InvocationTargetException 对被调用方法的包装异常
     * @IllegalAccessException 如果请求的方法不能通过反射访问
     * @NoSuchMethodException 如果找不到指定的可访问的方法
     * @since 1.0.0
     */
    fun getProperty(bean: Any, name: String?): Any? = PropertyUtils.getProperty(bean, name)

    //endregion 封装org.apache.commons.beanutils.BeanUtils和PropertyUtils

    init {
        ConvertUtils.register(DateConverter(null), java.util.Date::class.java)
        ConvertUtils.register(DateConverter(null), java.sql.Date::class.java)
        ConvertUtils.register(DateConverter(null), java.sql.Timestamp::class.java)
    }
}