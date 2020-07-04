package org.kuark.base.lang.reflect

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.reflect.MethodUtils
import org.kuark.base.lang.string.uncapitalize
import java.beans.PropertyDescriptor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.KClass

/**
 * 方法工具类
 *
 * @author K
 * @since 1.0.0
 */
object MethodKit {

    private const val SETTER_PREFIX = "set"
    private const val GETTER_PREFIX = "get"

    fun getWriteMethods(clazz: KClass<*>): List<Method> {
        val writeMethods = ArrayList<Method>()
        val methods = clazz.java.methods
        for (method in methods) {
            val name = method.name
            if (name.startsWith(SETTER_PREFIX)) {
                writeMethods.add(method)
            }
        }
        return writeMethods
    }

    /**
     * 取得所有可读的方法
     *
     * @param clazz 类
     * @return List<方法实例>
     * @since 1.0.0
     */
    fun getReadMethods(clazz: KClass<*>): List<Method> {
        val readMethods = ArrayList<Method>()
        val methods = clazz.java.methods
        for (method in methods) {
            val name = method.name
            val isGetter = name.startsWith(GETTER_PREFIX)
            if (isGetter && "getClass" != name) {
                readMethods.add(method)
            }
        }
        return readMethods

        //! 以下方法取的是父类(接口)中声明的方法，会存在返回值类型不准确(泛型)的问题
//		try {
//			BeanInfo beaninfo = Introspector.getBeanInfo(clazz);
//			PropertyDescriptor[] pds = beaninfo.getPropertyDescriptors();
//			for (PropertyDescriptor propDesc : pds) {
//				Method readMethod = propDesc.getReadMethod();
//				if (readMethod != null) {
//					methods.add(readMethod);
//				}
//			}
//			return methods;
//		} catch (Exception e) {
//			throw new SystemException(e);
//		}
    }

    /**
     * 调用指定属性的getter方法 <br></br>
     * 支持多级，如：对象名.对象名.属性
     *
     * @param obj 调用的对象
     * @param propertyName 属性名
     * @return 属性名对应get方法的返回值
     * @throws NoSuchMethodException 如果找不到指定的可访问的方法
     * @throws InvocationTargetException 对被调用方法的包装异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @since 1.0.0
     */
    fun invokeGetter(obj: Any, propertyName: String): Any? {
        var `object` = obj
        for (name in StringUtils.split(propertyName, ".")) {
            val getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name)
            `object` = invokeExactMethod(`object`, getterMethodName)
        }
        return `object`
    }

    /**
     * 获取属性的读方法(getter)
     *
     * @param clazz 类
     * @param property 属性名
     * @return getter
     */
    fun getReadMethod(clazz: KClass<*>, property: String?): Method {
        val getter = GETTER_PREFIX + StringUtils.capitalize(property)
        return clazz.java.getMethod(getter)

        //! 以下方法取的是父类(接口)中声明的方法，会存在返回值类型不准确(泛型)的问题
//		try {
//			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(property, clazz);
//			return propertyDescriptor.getReadMethod();
//		} catch (Exception e) {
//			throw new SystemException(e);
//		}
    }

    /**
     * 获取属性的写方法(setter)
     *
     * @param clazz 类
     * @param property 属性名
     * @return getter
     */
    fun getWriteMethod(clazz: KClass<*>, property: String): Method {
        val propertyDescriptor = PropertyDescriptor(property, clazz.java)
        return propertyDescriptor.writeMethod
    }

    /**
     * 调用指定属性的setter方法 <br></br>
     * 支持多级，如：对象名.对象名.属性
     *
     * @param obj 调用的对象
     * @param propertyName 属性名
     * @param value setter方法的参数值
     * @since 1.0.0
     */
    fun invokeSetter(obj: Any, propertyName: String, value: Any) {
        var `object` = obj
        val names = StringUtils.split(propertyName, ".")
        for (i in names.indices) {
            if (i < names.size - 1) {
                val getterMethodName =
                    GETTER_PREFIX + StringUtils.capitalize(names[i])
                `object` = invokeExactMethod(`object`, getterMethodName)
            } else {
                val setterMethodName =
                    SETTER_PREFIX + StringUtils.capitalize(names[i])
                invokeMethod(`object`, setterMethodName, *arrayOf(value))
            }
        }
    }

    /**
     * 获取get或is方法的对应属性
     *
     * @param readMethod get或is方法对象
     * @return 属性名
     */
    fun getReadProperty(readMethod: Method): String? {
        val methodName = readMethod.name
        if ("getClass" != methodName) {
            val prop = methodName.replaceFirst("^is|^get".toRegex(), "").uncapitalize()
            if (methodName != prop) {
                return prop
            }
        }
        return null
    }

    /**
     * 强制调用权限外(非public)的方法 <br></br>
     * 注：慎用！代码混淆后将可能调用不了
     *
     * @param target 调用的对象
     * @param methodName 方法名
     * @param args 可变方法参数
     * @return 方法返回值
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    fun forceInvoke(target: Any, methodName: String, argTypes: Array<KClass<*>>?, vararg args: Any?): Any {
        val javaClasses = arrayOfNulls<Class<*>>(argTypes?.size ?: 0)
        argTypes?.forEachIndexed { index, kClass -> javaClasses[index] = kClass.java }
        val m = target.javaClass.getDeclaredMethod(methodName, *javaClasses)
        m.isAccessible = true
        return m.invoke(target, *args)
    }

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.reflect.MethodUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 在指定的对象上调用一个指定方法名和匹配参数类型的方法
     * 该方法是[方法的代理][.getMatchingAccessibleMethod]
     */
    fun invokeMethod(`object`: Any, methodName: String, vararg args: Any?): Any =
        MethodUtils.invokeMethod(`object`, methodName, *args)

    /**
     * 在指定的对象上调用一个指定方法名和匹配参数类型的方法
     * 该方法是[方法的代理][.getMatchingAccessibleMethod]
     */
    fun invokeMethod(`object`: Any, methodName: String, args: Array<Any?>?, parameterTypes: Array<Class<*>?>?): Any =
        MethodUtils.invokeMethod(`object`, methodName, args, parameterTypes)

    /**
     * 在指定的对象上调用一个指定方法名和精确匹配参数类型的方法
     * 该方法使用反射来调用从`getAccessibleMethod()`获取的方法.
     *
     * @param object 方法的调用对象
     * @param methodName 方法名
     * @param args 参数值数组 - null将被当作空数组
     * @return 指定方法调用的返回值
     * @throws NoSuchMethodException 如果找不到指定的可访问的方法 <br></br>
     * @throws InvocationTargetException 对被调用方法的包装异常 <br></br>
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @since 1.0.0
     */
    fun invokeExactMethod(`object`: Any, methodName: String, vararg args: Any?): Any =
        MethodUtils.invokeExactMethod(`object`, methodName, *args)

    /**
     * 在指定的对象上调用一个指定方法名和精确匹配参数类型的方法
     * 该方法使用反射来调用从`getAccessibleMethod()`获取的方法.
     *
     *
     * @param obj 方法的调用对象
     * @param methodName 方法名
     * @param args 参数值数组 - null将被当作空数组
     * @param parameterTypes 参数类型数组 - null将被当作空数组
     * @return 指定方法调用的返回值
     * @throws NoSuchMethodException 如果找不到指定的可访问的方法
     * @throws InvocationTargetException 对被调用方法的包装异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @since 1.0.0
     */
    fun invokeExactMethod(obj: Any, methodName: String, args: Array<Any?>?, parameterTypes: Array<Class<*>?>?): Any =
        MethodUtils.invokeExactMethod(obj, methodName, args, parameterTypes)


    /**
     * 在指定的类上调用一个指定方法名和匹配参数类型的静态方法
     * 该方法使用反射来调用从`getAccessibleMethod()`获取的方法.
     *
     * @param cls 方法的调用类
     * @param methodName 方法名
     * @param args 参数值数组 - null将被当作空数组
     * @param paramTypes 参数类型数组 - null将被当作空数组
     * @return 指定方法调用的返回值
     * @throws NoSuchMethodException 如果找不到指定的可访问的方法
     * @throws InvocationTargetException 对被调用方法的包装异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @since 1.0.0
     */
    fun invokeExactStaticMethod(
        cls: KClass<*>, methodName: String, args: Array<Any?>?, paramTypes: Array<Class<*>?>?
    ): Any = MethodUtils.invokeExactStaticMethod(cls.java, methodName, args, paramTypes)

    /**
     * 在指定的类上调用一个指定方法名和匹配参数类型的静态方法
     * 该方法是[方法的代理][.getMatchingAccessibleMethod]
     */
    fun invokeStaticMethod(cls: KClass<*>, methodName: String, vararg args: Any?): Any =
        MethodUtils.invokeStaticMethod(cls.java, methodName, *args)

    /**
     * 在指定的类上调用一个指定方法名和匹配参数类型的静态方法
     * 该方法是[方法的代理][.getMatchingAccessibleMethod]
     */
    fun invokeStaticMethod(cls: KClass<*>, methodName: String, args: Array<Any?>?, paramTypes: Array<Class<*>?>?): Any =
        MethodUtils.invokeStaticMethod(cls.java, methodName, args, paramTypes)

    /**
     * 在指定的类上调用一个指定方法名和精确匹配参数类型的静态方法
     * 该方法使用反射来调用从`getAccessibleMethod(Class, String, Class[])`获取的方法.
     *
     * @param cls 方法的调用类
     * @param methodName 方法名
     * @param args 参数值数组 - null将被当作空数组
     * @return 指定方法调用的返回值
     * @throws NoSuchMethodException 如果找不到指定的可访问的方法
     * @throws InvocationTargetException 对被调用方法的包装异常
     * @throws IllegalAccessException 如果请求的方法不能通过反射访问
     * @since 1.0.0
     */
    fun invokeExactStaticMethod(cls: KClass<*>, methodName: String, vararg args: Any?): Any =
        MethodUtils.invokeExactStaticMethod(cls.java, methodName, *args)

    /**
     * 返回指定类的指定方法名和参数对应的可访问(能够通过反射调用)方法. <br></br>
     * 如果没有这样的方法, 返回`null`.
     * 该方法只是对 [.getAccessibleMethod] 方法的简单包装.
     *
     * @param cls 要获取的方法所在的类
     * @param methodName 方法名
     * @param parameterTypes 参数类型数组
     * @return 可访问的方法
     * @since 1.0.0
     */
    fun getAccessibleMethod(cls: KClass<*>, methodName: String, vararg parameterTypes: Class<*>?): Method =
        MethodUtils.getAccessibleMethod(cls.java, methodName, *parameterTypes)

    /**
     * 返回一个实现了指定方法的可访问(能够通过反射调用)的方法. <br></br>
     * 如果没有这样的方法, 返回`null`.
     *
     * @param method 希望调用的方法
     * @return 可访问的方法
     * @since 1.0.0
     */
    fun getAccessibleMethod(method: Method): Method = MethodUtils.getAccessibleMethod(method)

    /**
     * 查找一个匹配给定的方法名和可兼容参数的可访问的方法. <br></br>
     * 可兼容参数意味着每个方法参数都可从给定的参数进行赋值. 换句话, 要查找的方法要能够接受给定的参数值.
     * 该方法被
     * [.invokeMethod] 方法使用.
     * 该方法调用时传入包装类能够匹配带有基本类型参数的方法. 例如: 一个`Boolean`对象将匹配`boolean`基本类型.
     *
     * @param cls 要查找的方法所在的类
     * @param methodName 方法名
     * @param parameterTypes 可兼容的方法参数可变数组
     * @return 可访问的方法
     * @since 1.0.0
     */
    fun getMatchingAccessibleMethod(cls: KClass<*>, methodName: String, vararg parameterTypes: KClass<*>?): Method {
        val javaClasses = arrayOfNulls<Class<*>>(parameterTypes?.size ?: 0)
        parameterTypes?.forEachIndexed { index, kClass -> javaClasses[index] = kClass!!.java }
        return MethodUtils.getMatchingAccessibleMethod(cls.java, methodName, *javaClasses)
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.reflect.MethodUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}