package org.kuark.base.lang.reflect

import org.apache.commons.lang3.reflect.TypeUtils
import java.lang.reflect.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * 类型工具类
 *
 * @author K
 * @since 1.0.0
 */
object TypeKit {

    /**
     * 将String类型的值转为指定类型的值
     *
     * @param valueStr String类型的值
     * @param returnType 目标类型
     * @return 指定类型的值
     */
    fun valueOfStr(valueStr: String, returnType: Class<*>?): Any? { //TODO junit
        if (returnType == String::class.java) {
            return valueStr
        }
        if (valueStr.isBlank() && !returnType!!.isPrimitive || returnType == null) {
            return null
        }
        return if (returnType == Int::class.java) {
            Integer.valueOf(valueStr)
        } else if (returnType == Double::class.java) {
            java.lang.Double.valueOf(valueStr)
        } else if (returnType == Boolean::class.java) {
            valueStr.toBoolean()
        } else if (returnType == Date::class.java) {
            TODO()
//            LocaleDateKit.parse(valueStr, CommonContext.get().getLocale(), CommonContext.get().getTimeZone())
        } else if (returnType == Long::class.java) {
            java.lang.Long.valueOf(valueStr)
        } else if (returnType == Float::class.java) {
            java.lang.Float.valueOf(valueStr)
        } else if (returnType == BigDecimal::class.java) {
            BigDecimal(valueStr)
        } else if (returnType == Short::class.java) {
            valueStr.toShort()
        } else if (returnType == BigInteger::class.java) {
            valueStr.toBigDecimal()
        } else if (returnType == Byte::class.java) {
            java.lang.Byte.valueOf(valueStr)
        } else if (returnType == Char::class.java) {
            Character.getNumericValue(Integer.valueOf(valueStr))
        } else if (returnType == Int::class.javaPrimitiveType) {
            valueStr.toInt()
        } else if (returnType == Double::class.javaPrimitiveType) {
            valueStr.toDouble()
        } else if (returnType == Boolean::class.javaPrimitiveType) {
            valueStr.toBoolean()
        } else if (returnType == Long::class.javaPrimitiveType) {
            valueStr.toLong()
        } else if (returnType == Float::class.javaPrimitiveType) {
            valueStr.toFloat()
        } else if (returnType == Short::class.javaPrimitiveType) {
            valueStr.toShort()
        } else if (returnType == Byte::class.javaPrimitiveType) {
            valueStr.toByte()
        } else if (returnType == Char::class.javaPrimitiveType) {
            valueStr.toInt()
        } else {
            valueStr
        }
    }
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.reflect.TypeUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 检查一个类型是否能被在遵循java类型规则下隐式地转化成另一个类型. <br></br>
     * 如果两个类型都为[Class]对象, 该方法返回[ClassUtils.isAssignable]的结果.
     *
     * @param type 被赋值给目标类型的主题类型
     * @param toType 目标类型
     * @return `true` 如果 `type` 能够被赋值给 `toType`.
     * @since 1.0.0
     */
    fun isAssignable(type: Type?, toType: Type?): Boolean {
        return TypeUtils.isAssignable(type, toType)
    }

    /**
     * 获取指定参数化类型的所有类型参数, 包括所有者体系的参数, 如: `Outer<K></K>,V>.Inner<T>.DeepInner<E></E></T>` .<br></br>
     * 参数将以[Map]的形式返回, key为[TypeVariable]
     *
     *
     * @param type 指定的要获取类型参数的主题参数化类型
     * @return 类型参数的map
     * @since 1.0.0
     */
    fun getTypeArguments(type: ParameterizedType?): Map<TypeVariable<*>, Type> {
        return TypeUtils.getTypeArguments(type)
    }

    /**
     * 获取基于子类型的类或接口的类型参数. 例如, 该方法将确定[Map]接口的两个参数都是子类型[Properties][java.util.Properties]的对象,
     * 即使子类型没有直接实现`Map`接口.
     *
     * 该方法返回`null`如果`type`不能赋值给`toClass`.
     * 返回空的Map如果继承体系中没有任何的类或接口指定任何类型参数.
     *
     * 该方法的一个副作用为, 它同样会获取作为`type` 和 `toClass`层次结构的一部分类和接口的类型参数. <br></br>
     * 所以对于上述的例子, 该方法将同样确定[Hashtable][java.util.Hashtable]的类型参数是否都为`Object`. <br></br>
     * 在一些情况下, `toClass`指定的接口(间接)被实现不只一次(如: `toClass`指定为[Iterable]
     * 接口, `type`指定一个实现[Set]和[Collection][java.util.Collection]的参数化类型)<br></br>
     * 该方法将只在继承体系结构中查找一个实现或子类；遇到的第一个接口将不是`type` 到 `toClass`层次结构中的一个子接口.
     *
     *
     * @param type the type from which to determine the type parameters of toclass
     * @param toClass the class whose type parameters are to be determined based on the subtype `type`
     * @return a map of the type assignments for the type variables in each type in the inheritance hierarchy from
     * `type` to `toClass` inclusive.
     * @since 1.0.0
     */
    fun getTypeArguments(
        type: Type?,
        toClass: Class<*>?
    ): Map<TypeVariable<*>, Type> {
        return TypeUtils.getTypeArguments(type, toClass)
    }

    /**
     * 试图确定一个基于父类的参数化类型的类型参数
     * [.getTypeArguments]正好与该方法相反, 它获取一个类或接口的基于子类型的类型参数. <br></br>
     * 它在为主题的类的类型变量确定类型参数方面有更多限制, 它只能确定那些从主题[Class]对象映射到子类型的参数.
     *
     *  例如: [TreeSet][java.util.TreeSet] 设置它的参数作为[NavigableSet][java.util.NavigableSet]的参数, <br></br>
     * 这将反过来又设置[java.util.SortedSet]的参数, 这将反过来又设置{[Set]}的参数, 这将反过来又设置<br></br>
     * [java.util.Collection]的参数, [Iterable]的参数.因为`TreeSet`的参数映射为<br></br>
     * `Iterable`的参数, 它将可以确定基于父类型`Iterable>>`, <br></br>
     * `TreeSet`的参数为`? extends Map<Integer></Integer>,? extends Collection>`.
     *
     * @param cls the class whose type parameters are to be determined
     * @param superType the super type from which `cls`'s type arguments are to be determined
     * @return a map of the type assignments that could be determined for the type variables in each type in the
     * inheritance hierarchy from `type` to `toClass` inclusive.
     * @since 1.0.0
     */
    fun determineTypeArguments(
        cls: Class<*>?,
        superType: ParameterizedType?
    ): Map<TypeVariable<*>, Type> {
        return TypeUtils.determineTypeArguments(cls, superType)
    }

    /**
     * 检查是否给定的对象能按java的类型规则被赋值给目标类型
     *
     * @param value 要检查的对象
     * @param type 目标类型
     * @return true 如果`value` 为 `type`的一个实例
     * @since 1.0.0
     */
    fun isInstance(value: Any?, type: Type?): Boolean {
        return TypeUtils.isInstance(value, type)
    }

    /**
     * 此方法在类型变量类型和通配符类型（或带通配符的类型，如果有多个上限被允许)上去除冗余的上限类型 。
     *
     * 例如: 变量类型申明如下:
     *
     * <pre>
     * &lt;K extends java.util.Collection&lt;String&gt; &amp;
     * java.util.List&lt;String&gt;&gt;
     * </pre>
     *
     * 因为 `List` 是 `Collection`的一个子接口, 该方法返回的边界如下:
     *
     * <pre>
     * &lt;K extends java.util.List&lt;String&gt;&gt;
     * </pre>
     *
     * @param bounds 代表通配符或类型变量的上限类型的数组
     * @return 一个包含去除了冗余的上限类型的类型数组
     * @since 1.0.0
     */
    fun normalizeUpperBounds(bounds: Array<Type?>?): Array<Type> {
        return TypeUtils.normalizeUpperBounds(bounds)
    }

    /**
     * 返回一个包含[Object]的范围类型的数组, 如果[TypeVariable.getBounds]返回一个空数组. 否则, 返回将
     * `TypeVariable.getBounds()传给[.normalizeUpperBounds]方法的结果.
     *
     * @param typeVariable 主题类型变量
     * @return 一个包含类型变量边界的非空数组
     * @since 1.0.0
     */
    fun getImplicitBounds(typeVariable: TypeVariable<*>?): Array<Type> {
        return TypeUtils.getImplicitBounds(typeVariable)
    }

    /**
     * 返回一个包含惟一[Object]值的数组, 如果[WildcardType.getUpperBounds]返回一个空数组. <br></br>
     * 否则, 返回将`WildcardType.getUpperBounds()`传给[.normalizeUpperBounds]方法的结果.
     *
     *
     * @param wildcardType 主题通配符类型
     * @return 一个包含通配符的上限边界的非空数组
     * @since 1.0.0
     */
    fun getImplicitUpperBounds(wildcardType: WildcardType?): Array<Type> {
        return TypeUtils.getImplicitUpperBounds(wildcardType)
    }

    /**
     * 返回一个包含`null`单一值的数组, 如果[WildcardType.getLowerBounds]返回一个空数组.
     * 否则, 返回`WildcardType.getLowerBounds()`的结果.
     *
     * @param wildcardType 主题通配符类型
     * @return 一个包含通配符的下限边界的非空数组
     * @since 1.0.0
     */
    fun getImplicitLowerBounds(wildcardType: WildcardType?): Array<Type> {
        return TypeUtils.getImplicitLowerBounds(wildcardType)
    }

    /**
     * 确定是否指定类型满足它们映射的类型变量的边界. 当一个类型参数继承另一个(如`<T></T>, S extends T>`), <br></br>
     * 使用另一个当一个类型参数(如`<T></T>, S extends Comparable<T></T>`), 或依赖于另一个指定的类型变量, 该依赖必须<br></br>
     * 在被包含在 `typeVarAssigns` 中.
     *
     * @param typeVarAssigns 指定的要被分配给类型变量的潜在类型
     * @return 是否指定的类型能被赋值给各自的类型变量
     * @since 1.0.0
     */
    fun typesSatisfyVariables(typeVarAssigns: Map<TypeVariable<*>?, Type?>?): Boolean {
        return TypeUtils.typesSatisfyVariables(typeVarAssigns)
    }

    /**
     * 从上下文获取java的原始类型. 主要用于[TypeVariable]和[GenericArrayType], 或当你不知道`type`的 <br></br>
     * 运行时类型时: 如果你知道你有一个[Class]实例, 它已经是原始类型；如果你知道你有一个[ParameterizedType], <br></br>
     * 它的原始类型只有一个方法调用.
     *
     * @param type to resolve
     * @param assigningType type to be resolved against
     * @return the resolved `Class` object or `null` if the type could not be resolved
     * @since 1.0.0
     */
    fun getRawType(type: Type?, assigningType: Type?): Class<*> {
        return TypeUtils.getRawType(type, assigningType)
    }

    /**
     * 检测指定的类型是否为数组类型
     *
     * @param type 要检测的类型
     * @return `true` 如果 `type` 为数组类型 或 [GenericArrayType].
     * @since 1.0.0
     */
    fun isArrayType(type: Type?): Boolean {
        return TypeUtils.isArrayType(type)
    }

    /**
     * 获取数组的元素类型
     *
     * @param type 要检测的类型
     * @return 元素类型或如果指定的类型不是一个数组类型时返回null
     * @since 1.0.0
     */
    fun getArrayComponentType(type: Type?): Type {
        return TypeUtils.getArrayComponentType(type)
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.reflect.TypeUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

}