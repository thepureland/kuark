package io.kuark.base.lang

import io.kuark.base.lang.reflect.getMemberFunction
import io.kuark.base.lang.reflect.getMemberProperty
import io.kuark.base.support.ICallback
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.Serializable

/**
 * GenericKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class GenericKitTest {

    @Test
    fun getSuperClassGenricType() {
        // 没有父类，取实现的第一个接口上的泛型参数
        assertEquals(Int::class, GenericKit.getSuperClassGenricClass(TestGeneric1::class, 0))
        assertEquals(Map::class, GenericKit.getSuperClassGenricClass(TestGeneric1::class, 1))

        // 有父类(在父类参数化，会递归往父类取)
        assertEquals(Int::class, GenericKit.getSuperClassGenricClass(TestGeneric2::class, 0))
        assertEquals(Map::class, GenericKit.getSuperClassGenricClass(TestGeneric2::class, 1))

        // 有父类(在当前类参数化)
        assertEquals(String::class, GenericKit.getSuperClassGenricClass(TestGeneric3::class, 0))
        assertEquals(Double::class, GenericKit.getSuperClassGenricClass(TestGeneric3::class, 1))

        // 索引越界
        assertEquals(null, GenericKit.getSuperClassGenricClass(TestGeneric3::class, 2))
    }

    @Test
    fun getParameterTypeGeneric() {
        val function = TestGeneric3::class.getMemberFunction("f")

        // 第0个参数为TestGeneric2对象
        assertEquals(
            listOf(Nothing::class), GenericKit.getParameterTypeGenericClass(function, 0)
        )

        // 不支持泛型参数的类型
        assertEquals(listOf(Nothing::class), GenericKit.getParameterTypeGenericClass(function, 1))

        // 有具体泛型参数
        assertEquals(listOf(String::class, Boolean::class), GenericKit.getParameterTypeGenericClass(function, 2))

        // 指定为"*"
        assertEquals(listOf(Any::class, Any::class), GenericKit.getParameterTypeGenericClass(function, 3))

        // 越界
        assertEquals(null, GenericKit.getParameterTypeGenericClass(function, 4))
    }

    @Test
    fun getReturnTypeGeneric() {
        // 属性对象，没有泛型参数
        val prop0 = TestGeneric3::class.getMemberProperty("prop0")
        assertEquals(Nothing::class, GenericKit.getReturnTypeGenericClass(prop0, 0))

        // 属性对象，有具体泛型参数
        val prop1 = TestGeneric3::class.getMemberProperty("prop1")
        assertEquals(Int::class, GenericKit.getReturnTypeGenericClass(prop1, 0))
        assertEquals(Float::class, GenericKit.getReturnTypeGenericClass(prop1, 1))
        assertEquals(null, GenericKit.getReturnTypeGenericClass(prop1, 2)) // 越界

        // 属性对象，有泛型参数，但指定为"*"
        val prop2 = TestGeneric3::class.getMemberProperty("prop2")
        assertEquals(Any::class, GenericKit.getReturnTypeGenericClass(prop2, 0))
        assertEquals(Any::class, GenericKit.getReturnTypeGenericClass(prop2, 1))
        assertEquals(null, GenericKit.getReturnTypeGenericClass(prop2, 2)) // 越界

        // 函数对象，没有泛型参数
        val fun0 = TestGeneric3::class.getMemberFunction("fun0")
        assertEquals(Nothing::class, GenericKit.getReturnTypeGenericClass(fun0, 0))

        // 属性对象，有具体泛型参数
        val fun1 = TestGeneric3::class.getMemberFunction("fun1")
        assertEquals(Int::class, GenericKit.getReturnTypeGenericClass(fun1, 0))
        assertEquals(Float::class, GenericKit.getReturnTypeGenericClass(fun1, 1))
        assertEquals(null, GenericKit.getReturnTypeGenericClass(fun1, 2)) // 越界

        // 属性对象，有泛型参数，但指定为"*"
        val fun2 = TestGeneric3::class.getMemberFunction("fun2")
        assertEquals(Any::class, GenericKit.getReturnTypeGenericClass(fun2, 0))
        assertEquals(Any::class, GenericKit.getReturnTypeGenericClass(fun2, 1))
        assertEquals(null, GenericKit.getReturnTypeGenericClass(fun2, 2)) // 越界
    }

    internal class TestGeneric1 : ICallback<Int, Map<String, Boolean>>, Serializable {
        override fun execute(p: Int): Map<String, Boolean> = mapOf()
    }

    internal abstract class TestGeneric : ICallback<Int, Map<String, Boolean>>

    internal class TestGeneric2 : TestGeneric(), Serializable {
        override fun execute(p: Int): Map<String, Boolean> = mapOf()
    }

    internal class TestGeneric3(val prop0: String? = null) : Serializable, HashMap<String, Double>() {

        val prop1: Map<Int, Float>? = null

        val prop2: Map<Any, *>? = null

        fun fun0(): String? = null

        fun fun1(): Map<Int, Float>? = null

        fun fun2(): Map<Any, *>? = null

        fun f(str: String, map: Map<String, Boolean>, map1: Map<Any, *>) {}

    }

}