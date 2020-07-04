package org.kuark.base.lang

import org.kuark.base.log.LogFactory
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 * 泛型工具类
 *
 * @author K
 * @since 1.0.0
 */
object GenericKit {
    private val LOG = LogFactory.getLog(GenericKit::class)
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 整理SpringSide的Generics类
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    /**
     *
     *
     * 获取指定类的父类的泛型参数的实际类型. 如: NameAndAge extends Pair<String></String>, Integer>
     *
     *
     * @param clazz 需要获取泛型参数实际类型的类, 该类必须继承泛型父类
     * @param index 泛型参数所在索引, 从0开始.
     * @return 泛型参数的实际类型. 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回`Object.class`, 如果索引越界返回null
     * @since 1.0.0
     */
    fun getSuperClassGenricType(clazz: Class<*>, index: Int): Class<*>? {
        if (clazz == Any::class.java) {
            return null
        }
        var genType = clazz.genericSuperclass // 得到泛型父类
        if (genType == null) {
            val genericInterfaces = clazz.genericInterfaces
            if (genericInterfaces.isNotEmpty()) {
                genType = genericInterfaces[0]
            }
        }
        // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
        if (genType !is ParameterizedType) {
            val superclass = clazz.superclass
            return if (superclass == Any::class.java) {
                Any::class.java
            } else {
                getSuperClassGenricType(superclass, index) // 往父类取
            }
        }
        // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class,
        // 如NameAndAge extends Pair<String, Integer>就返回String和Integer类型
        val params =
            genType.actualTypeArguments
        if (index < 0 || index >= params.size) {
            LOG.error("输入的索引" + if (index < 0) "不能小于0" else "超出了参数的总数")
            return null
        }
        return if (params[index] is Class<*> == false) {
            Any::class.java
        } else params[index] as Class<*>
    }

    /**
     *
     *
     * 获取指定类的父类的第0个泛型参数的实际类型. 如: NameAndAge extends Pair<String></String>, Integer>将返回Strin.class
     *
     *
     * @param clazz 需要获取泛型参数实际类型的类, 该类必须继承泛型父类
     * @return 泛型参数的实际类型. 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回`Object.class`
     * @since 1.0.0
     */
    fun getSuperClassGenricType(clazz: Class<*>): Class<*>? {
        return getSuperClassGenricType(clazz, 0)
    }

    /**
     *
     *
     * 获取方法返回值泛型参数的实际类型. 如: public Map<String></String>, Integer> getNameAndAge()
     *
     *
     * @param method 方法
     * @param index 泛型参数所在索引, 从0开始.
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回`Object.class`, 如果索引越界返回null
     * @since 1.0.0
     */
    fun getMethodGenericReturnType(method: Method, index: Int): Class<*>? {
        val returnType = method.genericReturnType
        if (returnType is ParameterizedType) {
            val typeArguments = returnType.actualTypeArguments
            if (index < 0 || index >= typeArguments.size) {
                LOG.error("输入的索引" + if (index < 0) "不能小于0" else "超出了参数的总数")
                return null
            }
            return typeArguments[index] as Class<*>
        }
        return Any::class.java
    }

    /**
     *
     *
     * 获取方法返回值的第0个泛型参数的实际类型. 如: public Map<String></String>, Integer> getNameAndAge()将返回String.class
     *
     *
     * @param method 方法
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回`Object.class`
     * @since 1.0.0
     */
    fun getMethodGenericReturnType(method: Method): Class<*>? {
        return getMethodGenericReturnType(method, 0)
    }

    /**
     *
     *
     * 获取方法输入参数第index个输入参数的所有泛型参数的实际类型. 如: public void add(Map<String></String>, Integer> maps, List<String> names){}
    </String> *
     *
     * @param method 方法
     * @param index 第几个输入参数
     * @return 输入参数的泛型参数的实际类型列表, 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回空列表, 如果索引越界返回null
     * @since 1.0.0
     */
    fun getMethodGenericParameterTypes(
        method: Method,
        index: Int
    ): List<Class<*>>? {
        val results: MutableList<Class<*>> = ArrayList()
        val genericParameterTypes = method.genericParameterTypes
        if (index < 0 || index >= genericParameterTypes.size) {
            LOG.error("输入的索引" + if (index < 0) "不能小于0" else "超出了参数的总数")
            return null
        }
        val genericParameterType = genericParameterTypes[index]
        if (genericParameterType is ParameterizedType) {
            val parameterArgTypes = genericParameterType.actualTypeArguments
            for (parameterArgType in parameterArgTypes) {
                val parameterArgClass = parameterArgType as Class<*>
                results.add(parameterArgClass)
            }
            return results
        }
        return results
    }

    /**
     *
     *
     * 获取方法输入参数第0个输入参数的所有泛型参数的实际类型. 如: public void add(Map<String></String>, Integer> maps, List<String> names){}
    </String> *
     *
     * @param method 方法
     * @return 输入参数的泛型参数的实际类型列表, 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回空列表
     * @since 1.0.0
     */
    fun getMethodGenericParameterTypes(method: Method): List<Class<*>>? {
        return getMethodGenericParameterTypes(method, 0)
    }

    /**
     *
     *
     * 获取字段泛型参数的实际类型. 如: public Map<String></String>, Integer> nameAndAge;
     *
     *
     * @param field 字段
     * @param index 泛型参数所在索引, 从0开始.
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回`Object.class`, 如果索引越界返回null
     * @since 1.0.0
     */
    fun getFieldGenericType(field: Field, index: Int): Class<*>? {
        val genericFieldType = field.genericType
        if (genericFieldType is ParameterizedType) {
            val fieldArgTypes = genericFieldType.actualTypeArguments
            if (index < 0 || index >= fieldArgTypes.size) {
                LOG.error("输入的索引" + if (index < 0) "不能小于0" else "超出了参数的总数")
                return null
            }
            return fieldArgTypes[index] as Class<*>
        }
        return Any::class.java
    }

    /**
     *
     *
     * 获取字段第0个泛型参数的实际类型. 如: public Map<String></String>, Integer> nameAndAge;将返回String.class
     *
     *
     * @param field 字段
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回`Object.class`
     * @since 1.0.0
     */
    fun getFieldGenericType(field: Field): Class<*>? {
        return getFieldGenericType(field, 0)
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 整理SpringSide的Generics类
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}