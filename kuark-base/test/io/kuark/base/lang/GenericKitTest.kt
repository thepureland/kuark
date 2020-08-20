package io.kuark.base.lang

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import io.kuark.base.lang.reflect.FieldKit
import io.kuark.base.lang.reflect.MethodKit
import io.kuark.base.lang.reflect.PropertyKit
import io.kuark.base.support.ICallback
import java.io.Serializable
import kotlin.reflect.KClass

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
        assertEquals(Int::class, GenericKit.getSuperClassGenricType(TestGeneric1::class, 0))
        assertEquals(Map::class, GenericKit.getSuperClassGenricType(TestGeneric1::class, 1))

        // 有父类
        assertEquals(String::class, GenericKit.getSuperClassGenricType(TestGeneric2::class, 0))
        assertEquals(Double::class, GenericKit.getSuperClassGenricType(TestGeneric2::class, 1))

        // 索引越界
        assertEquals(null, GenericKit.getSuperClassGenricType(TestGeneric2::class, 2))
    }

    @Test
    fun getMethodGenericReturnType() {
        val method = MethodKit.getAccessibleMethod(TestGeneric1::class, "execute", Int::class)
        assertEquals(String::class, GenericKit.getMethodGenericReturnType(method, 0))
        assertEquals(null, GenericKit.getMethodGenericReturnType(method, 2))
    }

    @Test
    fun getMethodGenericParameterTypes() {
        val method = MethodKit.getAccessibleMethod(TestGeneric2::class, "m", String::class, Map::class)
        assertEquals(listOf<KClass<*>>(), GenericKit.getMethodGenericParameterTypes(method, 0))
        assertEquals(listOf(String::class, Boolean::class), GenericKit.getMethodGenericParameterTypes(method, 1))
        assertEquals(null, GenericKit.getMethodGenericParameterTypes(method, 2))
    }

    @Test
    fun getFieldGenericType() {
        val field = FieldKit.getDeclaredField(TestGeneric2::class, "field", true)!!
        assertEquals(Int::class, GenericKit.getPropertyGenericType(field, 0))
        assertEquals(Float::class, GenericKit.getPropertyGenericType(field, 1))
        assertEquals(null, GenericKit.getPropertyGenericType(field, 2))
    }

    internal class TestGeneric1: ICallback<Int, Map<String, Boolean>>, Serializable {
        override fun execute(p: Int): Map<String, Boolean> = mapOf()
    }

    internal class TestGeneric2: Serializable, HashMap<String, Double>() {

        val field: Map<Int, Float>? = null

        fun m(str: String, map: Map<String, Boolean>) {
        }

    }

}