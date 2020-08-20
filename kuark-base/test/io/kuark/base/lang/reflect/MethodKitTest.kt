package io.kuark.base.lang.reflect

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MethodKitTest {

    @Test
    fun testGetReadMethods() {
        val readMethods = MethodKit.getReadMethods(TestBean::class)
        val list = readMethods.map { it.name }
        assert(list.contains("getPublicField"))
    }

    @Test
    fun invokeGetterAndSetter() {
        var bean = TestBean()
        assertEquals(bean.inspectPublicField() + 1, MethodKit.invokeGetter(bean, "publicField"))
        bean = TestBean()
        // 通过setter的函数将+1
        MethodKit.invokeSetter(bean, "publicField", 10)
        assertEquals(10 + 1.toLong(), bean.inspectPublicField().toLong())
    }

    @Test
    fun testGetReadProperty() {
    }

    open class ParentBean<T, ID>

    class TestBean : ParentBean<String?, Long?>() {
        /**
         * 没有getter/setter的field
         */
        private val privateField = 1

        /**
         * 有getter/setter的field
         */
        private var publicField = 1

        fun getPublicField(): Int {
            return publicField + 1
        }

        fun setPublicField(publicField: Int) {
            this.publicField = publicField + 1
        }

        fun inspectPrivateField(): Int {
            return privateField
        }

        fun inspectPublicField(): Int {
            return publicField
        }

        private fun privateMethod(text: String): String {
            return "hello $text"
        }
    }

    class TestBean2 : ParentBean<Any?, Any?>()

    class TestBean3 {
        var id = 0
    }

}