package org.kuark.base.lang

import org.kuark.base.log.LogFactory
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

/**
 * 泛型工具类
 *
 * @author K
 * @since 1.0.0
 */
object GenericKit {

    private val LOG = LogFactory.getLog(GenericKit::class)


    /**
     * 获取指定类的父类的泛型参数的实际类型, 如果没有非Any的父类，取实现的第一个接口。
     *
     * @param clazz 需要获取泛型参数实际类型的类, 该类必须继承泛型父类或实现泛型接口
     * @param index 泛型参数所在索引, 从0开始.
     * @return 泛型参数的实际类型. 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回Any::class, 如果索引越界返回null
     * @since 1.0.0
     */
    fun getSuperClassGenricType(clazz: KClass<*>, index: Int = 0): KClass<*>? {
        if (clazz == Any::class) {
            return null
        }
        var genType = clazz.java.genericSuperclass // 得到泛型父类
        if (genType == Any::class.java) {
            // 没有非Any的父类，取实现的第一个接口
            val genericInterfaces = clazz.java.genericInterfaces
            if (genericInterfaces.isNotEmpty()) {
                genType = genericInterfaces[0]
            }
        }
        // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Any::class
        if (genType !is ParameterizedType) {
            val superclass = clazz.java.superclass as Class<*>
            return if (superclass == Any::class.java) {
                Any::class
            } else {
                getSuperClassGenricType(superclass.kotlin, index) // 往父类取
            }
        }
        // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class,
        // 如NameAndAge extends Pair<String, Integer>就返回String和Integer类型
        val params = genType.actualTypeArguments
        if (index < 0 || index >= params.size) {
            LOG.error("输入的索引" + if (index < 0) "不能小于0" else "超出了参数的总数")
            return null
        }
        return if (params[index] !is Class<*>) {
            val typeName = (params[index] as ParameterizedType).rawType.typeName
            (Class.forName(typeName) as Class<*>).kotlin
        } else (params[index] as Class<*>).kotlin
    }

    /**
     * 获取方法返回值泛型参数的实际类型.
     *
     * @param method 方法
     * @param index 泛型参数所在索引, 从0开始.
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回Any::class, 如果索引越界返回null
     * @since 1.0.0
     */
    fun getMethodGenericReturnType(method: Method, index: Int = 0): KClass<*>? {
        val returnType = method.genericReturnType
        if (returnType is ParameterizedType) {
            val typeArguments = returnType.actualTypeArguments
            if (index < 0 || index >= typeArguments.size) {
                LOG.error("输入的索引" + if (index < 0) "不能小于0" else "超出了参数的总数")
                return null
            }
            return (typeArguments[index] as Class<*>).kotlin
        }
        return Any::class
    }

    /**
     * 获取方法输入参数第index个输入参数的所有泛型参数的实际类型
     *
     * @param method 方法
     * @param index 第几个输入参数
     * @return 输入参数的泛型参数的实际类型列表, 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回空列表, 如果索引越界返回null
     * @since 1.0.0
     */
    fun getMethodGenericParameterTypes(method: Method, index: Int = 0): List<KClass<*>>? {
        val results = mutableListOf<KClass<*>>()
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
                results.add(parameterArgClass.kotlin)
            }
            return results
        }
        return results
    }

    /**
     * 获取字段泛型参数的实际类型.
     *
     * @param field 字段
     * @param index 泛型参数所在索引, 从0开始.
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，将直接返回Any::class, 如果索引越界返回null
     * @since 1.0.0
     */
    fun getPropertyGenericType(field: Field, index: Int = 0): KClass<*>? {
        val genericFieldType = field.genericType
        if (genericFieldType is ParameterizedType) {
            val fieldArgTypes = genericFieldType.actualTypeArguments
            if (index < 0 || index >= fieldArgTypes.size) {
                LOG.error("输入的索引" + if (index < 0) "不能小于0" else "超出了参数的总数")
                return null
            }
            return (fieldArgTypes[index] as Class<*>).kotlin
        }
        return Any::class
    }

}