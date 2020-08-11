package org.kuark.base.lang.reflect

import org.apache.commons.lang3.reflect.FieldUtils
import java.lang.reflect.Field
import kotlin.reflect.KClass

/**
 * 字段工具类
 *
 * @author K
 * @since 1.0.0
 */
object FieldKit {

    /**
     * 强制读取权限外(非public)的字段值
     * 注：慎用！代码混淆后将可能读不了
     *
     * @param target 调用的对象
     * @param fieldName 字段名
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    fun forceRead(target: Any, fieldName: String): Any? {
        val f = target.javaClass.getDeclaredField(fieldName)
        f.isAccessible = true
        return f[target]
    }

    /**
     * 强制写入权限外(非public)的字段值
     * 注：慎用！代码混淆后将可能写不了
     *
     * @param target 调用的对象
     * @param fieldName 字段名
     * @param value 字段值
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    fun forceWrite(target: Any, fieldName: String, value: Any?) {
        val f = target.javaClass.getDeclaredField(fieldName)
        f.isAccessible = true
        f[target] = value
    }

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.reflect.FieldUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 根据字段名, 取得指定类(包括其父类)对应的可访问字段, 可以指定是否突破范围限制
     *
     * @param cls 要反射的类, 不能为null
     * @param fieldName 要获取的字段的名称
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @return 字段对象
     * @since 1.0.0
     */
    fun getField(cls: KClass<*>, fieldName: String, forceAccess: Boolean = false): Field? =
        FieldUtils.getField(cls.java, fieldName, forceAccess)

    /**
     * 根据字段名, 取得指定类本身(不包括其父类)对应的可访问字段, 可以指定是否突破范围限制
     *
     * @param cls 要反射的类, 不能为null
     * @param fieldName 要获取的字段的名称
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @return 字段对象
     * @since 1.0.0
     */
    fun getDeclaredField(cls: KClass<*>, fieldName: String, forceAccess: Boolean = false): Field? =
        FieldUtils.getDeclaredField(cls.java, fieldName, forceAccess)

    /**
     * 取得一个可访问的静态字段的值, 可以指定是否突破范围限制
     *
     * @param field 要读取的字段
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制。缺省为false
     * @return 字段值
     * @since 1.0.0
     */
    fun readStaticField(field: Field, forceAccess: Boolean = false): Any? = FieldUtils.readStaticField(field, forceAccess)

    /**
     * 取得指定类(包括父类)指定字段名的静态字段的值, 可以指定是否突破范围限制
     *
     * @param cls 要反射的类, 不能为null
     * @param fieldName 要获取的字段的名称
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @return 字段对象
     * @throws IllegalArgumentException 字段找不到或不可访问
     * @since 1.0.0
     */
    fun readStaticField(cls: KClass<*>, fieldName: String, forceAccess: Boolean = false): Any? =
        FieldUtils.readStaticField(cls.java, fieldName, forceAccess)

    /**
     * 取得指定类(不包括父类)指定字段名的静态字段的值, 可以指定是否突破范围限制
     *
     * @param cls 要反射的类, 不能为null
     * @param fieldName 要获取的字段的名称
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @return 字段对象
     * @throws IllegalArgumentException 字段找不到或不可访问
     * @since 1.0.0
     */
    fun readDeclaredStaticField(cls: KClass<*>, fieldName: String, forceAccess: Boolean = false): Any? =
        FieldUtils.readDeclaredStaticField(cls.java, fieldName, forceAccess)

    /**
     * 读取指定对象的指定字段的值, 可以指定是否突破范围限制
     *
     * @param field 字段对象
     * @param target 目标对象, 如果为null表示读取的是静态字段
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制。缺省为false
     * @return 字段值
     * @throws IllegalArgumentException 如果字段不可访问
     * @since 1.0.0
     */
    fun readField(field: Field, target: Any?, forceAccess: Boolean = false): Any? =
        FieldUtils.readField(field, target, forceAccess)

    /**
     * 读取指定对象的指定public字段的值, 对指定对象的类的父类字段有效, 可以指定是否突破范围限制
     *
     * @param target 要反射的目标对象, 不能为null
     * @param fieldName 要获取的字段的名称
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @return 字段值
     * @throws IllegalArgumentException 如果指定字段不可访问
     * @since 1.0.0
     */
    fun readField(target: Any, fieldName: String, forceAccess: Boolean = false): Any? =
        FieldUtils.readField(target, fieldName, forceAccess)

    /**
     * 读取指定对象的指定public字段的值, 仅对指定对象的类本身字段有效, 可以指定是否突破范围限制
     *
     * @param target 要反射的目标对象, 不能为null
     * @param fieldName 要获取的字段的名称
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @return 字段对象
     * @throws IllegalArgumentException 如果指定字段不可访问
     * @since 1.0.0
     */
    fun readDeclaredField(target: Any, fieldName: String, forceAccess: Boolean = false): Any? =
        FieldUtils.readDeclaredField(target, fieldName, forceAccess)

    /**
     * 写入一个public的静态字段, 可以指定是否突破范围限制
     *
     * @param field 要写入的字段
     * @param value 要设置的值
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制`False`将仅匹配public的字段。缺省为false
     * @throws IllegalArgumentException 如果字段不是静态的或该字段不可访问或为final
     * @since 1.0.0
     */
    fun writeStaticField(field: Field, value: Any?, forceAccess: Boolean = false) =
        FieldUtils.writeStaticField(field, value, forceAccess)

    /**
     * 写入一个public的静态字段, 父类字段有效, 可以指定是否突破范围限制
     *
     * @param cls 要查找的字段的类
     * @param fieldName 要写入的字段的名称
     * @param value 要设置的值
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @throws IllegalArgumentException 如果字段找不到, 或不是静态的, 或不可访问, 或为final的
     *
     * @since 1.0.0
     */
    fun writeStaticField(cls: KClass<*>, fieldName: String, value: Any?, forceAccess: Boolean = false) =
        FieldUtils.writeStaticField(cls.java, fieldName, value, forceAccess)

    /**
     * 写入一个public的静态字段, 仅指定类本身字段有效, 可以指定是否突破范围限制
     *
     * @param cls 要查找的字段的类
     * @param fieldName 要写入的字段的名称
     * @param value 要设置的值
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @throws IllegalArgumentException 如果字段找不到, 或不是静态的, 或不可访问, 或为final的
     * @since 1.0.0
     */
    fun writeDeclaredStaticField(cls: KClass<*>, fieldName: String, value: Any?, forceAccess: Boolean = false) =
        FieldUtils.writeDeclaredStaticField(cls.java, fieldName, value, forceAccess)

    /**
     * 将指定值写入目标对象的指定字段, 可以指定是否突破范围限制
     *
     * @param field 要写入的字段
     * @param target 目标对象, 如果为null表示读取的是静态字段
     * @param value 要设置的值
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @throws IllegalArgumentException 如果字段不可访问, 或为final的
     * @since 1.0.0
     */
    fun writeField(field: Field, target: Any?, value: Any?, forceAccess: Boolean = false) =
        FieldUtils.writeField(field, target, value, forceAccess)

    /**
     * 将指定值写入目标对象的指定字段, 父类字段有效, 可以指定是否突破范围限制
     *
     * @param target 要反射的目标对象, 不能为null
     * @param fieldName 要获取的字段的名称
     * @param value 要设置的值
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @throws IllegalArgumentException 如果字段不可访问
     * @since 1.0.0
     */
    fun writeField(target: Any, fieldName: String, value: Any?, forceAccess: Boolean = false) =
        FieldUtils.writeField(target, fieldName, value, forceAccess)

    /**
     * 将指定值写入目标对象的指定public字段, 仅指定对象的类本身的字段有效, 可以指定是否突破范围限制
     *
     * @param target 要反射的目标对象, 不能为null
     * @param fieldName 要获取的字段的名称
     * @param value 要设置的值
     * @param forceAccess 是否使用`setAccessible`方法突破范围限制, `False`将仅匹配public的字段。缺省为false
     * @throws IllegalArgumentException 如果字段不可访问
     * @since 1.0.0
     */
    fun writeDeclaredField(target: Any, fieldName: String, value: Any?, forceAccess: Boolean = false) =
        FieldUtils.writeDeclaredField(target, fieldName, value, forceAccess)

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.reflect.FieldUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}