package io.kuark.base.lang

import io.kuark.base.lang.reflect.firstMatchTypeOf
import io.kuark.base.lang.reflect.getSuperClass
import io.kuark.base.lang.reflect.getSuperInterfaces
import java.lang.IllegalArgumentException
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure

/**
 * 泛型工具类
 *
 * @author K
 * @since 1.0.0
 */
object GenericKit {


    /**
     * 获取指定类的直接父类的泛型参数的实际类型, 如果没有非Any的父类，取实现的第一个接口。
     *
     * @param clazz 需要获取泛型参数实际类型的类, 该类必须继承泛型父类或实现泛型接口
     * @param index 泛型参数所在索引, 从0开始.
     * @return 泛型参数的实际类型。如果不支持泛型，将返回Nothing::class;泛型参数如果为"*"，将返回Any::class;如果索引越界将返回null
     * @since 1.0.0
     */
    fun getSuperClassGenricClass(clazz: KClass<*>, index: Int = 0): KClass<*> {
        if (clazz == Any::class) {
            return Nothing::class
        }

        var directSuperType = clazz.getSuperClass()!!.firstMatchTypeOf(clazz.supertypes)
        if (directSuperType == Any::class.starProjectedType) {
            // 没有非Any的父类，取实现的第一个接口
            val genericInterfaces = clazz.getSuperInterfaces()
            if (genericInterfaces.isNotEmpty()) {
                directSuperType = genericInterfaces[0].firstMatchTypeOf(clazz.supertypes)
            }
        }

        val args = directSuperType.arguments
        if(args.isEmpty()) {
            // 可能是在父类做的参数化
            return getSuperClassGenricClass(clazz.getSuperClass()!!, index) // 往父类取
        }

        if (args.isEmpty()) return Nothing::class
        if (index < 0 || index >= args.size) {
            throw IllegalArgumentException("输入的索引" + if (index < 0) "不能小于0" else "超出了参数的总数")
        }
        val type = args[index].type
        return type?.jvmErasure ?: Any::class // 泛型参数为*时返回Any::class
    }


    /**
     * 获取可调用对象第index个入参类型的所有泛型参数的实际类型
     *
     * @param callable 可调用对象，比如：函数
     * @param index 第几个输入参数。第0个输入参数为调用该函数的对象，隐式传递，所以显示的函数参数是从第1个算起
     * @return 泛型参数的实际类型列表。如果不支持泛型，元素类型将为Nothing::class;泛型参数如果为"*"，元素类型将为Any::class;如果索引越界元素将为null
     * @since 1.0.0
     */
    fun getParameterTypeGenericClass(callable: KCallable<*>, index: Int = 1): List<KClass<*>>? {
        if (index < 0 || index >= callable.parameters.size) {
            throw IllegalArgumentException("输入的索引" + if (index < 0) "不能小于0" else "超出了参数的总数")
        }
        val param = callable.parameters[index]
        val args = param.type.arguments
        if (args.isEmpty()) return listOf(Nothing::class)
        return args.map { it.type?.jvmErasure ?: Any::class }
    }

    /**
     * 获取可调用对象返回值泛型参数的实际类型.
     *
     * @param callable 可调用对象，比如：属性、函数
     * @param index 泛型参数所在索引, 从0开始.
     * @return 泛型参数的实际类型。如果不支持泛型，将返回Nothing::class;泛型参数如果为"*"，将返回Any::class;如果索引越界将返回null
     * @since 1.0.0
     */
    fun getReturnTypeGenericClass(callable: KCallable<*>, index: Int = 0): KClass<*>? {
        val args = callable.returnType.arguments
        if (args.isEmpty()) {
            return Nothing::class
        }
        if (index < 0 || index >= args.size) {
            throw IllegalArgumentException("输入的索引" + if (index < 0) "不能小于0" else "超出了泛型参数的总数")
        }
        val type = args[index].type
        return type?.jvmErasure ?: Any::class // 泛型参数为*时返回Any::class
    }

}