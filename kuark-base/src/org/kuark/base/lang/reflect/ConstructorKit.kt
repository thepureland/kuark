package org.kuark.base.lang.reflect

import org.apache.commons.lang3.reflect.ConstructorUtils
import java.lang.reflect.Constructor
import kotlin.reflect.KClass

/**
 * 构造方法工具类
 *
 * @author K
 * @since 1.0.0
 */
object ConstructorKit {

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.reflect.ConstructorUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 返回指定类的一个新的实例, 使用指定的构造器参数
     * 该方法查找并调用构造器. 构造器的签名必须赋值兼容地匹配参数类型.
     *
     * @param <T> 要创建的实例的类型
     * @param cls 要创建的实例的类, 不能为null
     * @param args 构造器的参数可变数组, null将被当作空数组
     * @return 指定类的新实例, 不会为null
     * NoSuchMethodException 如果找不到匹配的构造器 <br></br>
     * IllegalAccessException 如果调用不被安全体制允许 <br></br>
     * InvocationTargetException 如果调用时发生错误 <br></br>
     * InstantiationException 如果实例化时发生错误
     * @see .invokeConstructor
     * @since 1.0.0
     */
    fun <T : Any> invokeConstructor(cls: KClass<T>, vararg args: Any?): T =
        ConstructorUtils.invokeConstructor(cls.java, *args)

    /**
     * 返回指定类的一个新的实例, 使用指定的构造器参数和参数类型
     * 该方法查找并调用构造器. 构造器的签名必须赋值兼容地匹配参数类型.
     *
     * @param <T> 要创建的实例的类型
     * @param cls 要创建的实例的类, 不能为null
     * @param args 构造器的参数可变数组, null将被当作空数组
     * @param parameterTypes 参数类型数组, null将被当作空数组
     * @return 指定类的新实例, 不会为null
     * NoSuchMethodException 如果找不到匹配的构造器 <br></br>
     * IllegalAccessException 如果调用不被安全体制允许 <br></br>
     * InvocationTargetException 如果调用时发生错误 <br></br>
     * InstantiationException 如果实例化时发生错误
     * @see Constructor.newInstance
     * @since 1.0.0
     */
    fun <T : Any> invokeConstructor(cls: KClass<T>, args: Array<Any?>?, parameterTypes: Array<Class<*>?>?): T =
        ConstructorUtils.invokeConstructor(cls.java, args, parameterTypes)

    /**
     * 返回指定类的一个新的实例, 使用指定的构造器参数
     * 该方法查找并调用构造器. 构造器的签名必须精确地匹配参数类型.
     *
     * @param <T> 要创建的实例的类型
     * @param cls 要创建的实例的类, 不能为null
     * @param args 构造器的参数可变数组, null将被当作空数组
     * @return 指定类的新实例, 不会为null
     * NoSuchMethodException 如果找不到匹配的构造器 <br></br>
     * IllegalAccessException 如果调用不被安全体制允许 <br></br>
     * InvocationTargetException 如果调用时发生错误 <br></br>
     * InstantiationException 如果实例化时发生错误
     * @see .invokeExactConstructor
     * @since 1.0.0
     */
    fun <T : Any> invokeExactConstructor(cls: KClass<T>, vararg args: Any?): T =
        ConstructorUtils.invokeExactConstructor(cls.java, *args)

    /**
     * 返回指定类的一个新的实例, 使用指定的构造器参数和参数类型
     * 该方法查找并调用构造器. 构造器的签名必须精确地匹配参数类型.
     *
     * @param <T> 要创建的实例的类型
     * @param cls 要创建的实例的类, 不能为null
     * @param args 构造器的参数可变数组, null将被当作空数组
     * @param parameterTypes 参数类型数组, null将被当作空数组
     * @return 指定类的新实例, 不会为null
     * @throws SystemException 该异常是对下面几种异常的可能包装, 要得知真正的异常请获取该异常的cause: <br></br>
     * NoSuchMethodException 如果找不到匹配的构造器 <br></br>
     * IllegalAccessException 如果调用不被安全体制允许 <br></br>
     * InvocationTargetException 如果调用时发生错误 <br></br>
     * InstantiationException 如果实例化时发生错误
     * @see Constructor.newInstance
     * @since 1.0.0
     */
    fun <T : Any> invokeExactConstructor(cls: KClass<T>, args: Array<Any?>?, parameterTypes: Array<Class<*>?>?): T =
        ConstructorUtils.invokeExactConstructor(cls.java, args, parameterTypes)

    /**
     * 查找指定类和签名的构造器, 会检查它的可访问性
     * 该方法查找构造器并保证它是可访问的. 构造器签名必须精确匹配给定的参数类型.
     *
     * @param <T> 构造器类型
     * @param cls 要查找的构造器的类, 不能为null
     * @param parameterTypes 参数类型数组, null将被当作空数组
     * @return 构造器, 如果没有找到匹配的构造器则返回null
     * @see Class.getConstructor
     * @see .getAccessibleConstructor
     * @since 1.0.0
     */
    fun <T : Any> getAccessibleConstructor(cls: KClass<T>, vararg parameterTypes: Class<*>?): Constructor<T> =
        ConstructorUtils.getAccessibleConstructor(cls.java, *parameterTypes)

    /**
     * 检查指定的构造器是否可访问
     * 该方法简单的确保指定的构造器是否可访问
     *
     * @param <T> 构造器类型
     * @param ctor 构造器对象, 不能为null
     * @return 构造器, 如果没有找到匹配的可访问的构造器则返回null
     * @see SecurityManager
     * @since 1.0.0
     */
    fun <T> getAccessibleConstructor(ctor: Constructor<T>): Constructor<T> =
        ConstructorUtils.getAccessibleConstructor(ctor)

    /**
     * 查找指定类和签名的构造器, 会检查它的可访问性
     * 该方法检查所有构造器, 并找出一个能匹配给定参数类型的构造器. 它要求每一个参数都能够从给定的参数赋值.
     * 这是一种比正常的精确匹配算法更为灵活的查找方式.
     * 首先检查是否存在严格匹配的签名, 如果没有, 检查所有构造器看是否有赋值兼容的构造器. 第一个找到的构造器将被返回.
     *
     * @param <T> 构造器类型
     * @param cls 要查找的构造器的类, 不能为null
     * @param parameterTypes 匹配的参数类型可变数组
     * @return 构造器, 如果没有找到匹配的构造器则返回null
     * @since 1.0.0
     */
    fun <T : Any> getMatchingAccessibleConstructor(cls: KClass<T>, vararg parameterTypes: Class<*>?): Constructor<T> =
        ConstructorUtils.getMatchingAccessibleConstructor(cls.java, *parameterTypes)

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.reflect.ConstructorUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}