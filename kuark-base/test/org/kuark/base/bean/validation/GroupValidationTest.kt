package org.kuark.base.bean.validation

import org.hibernate.validator.constraints.Length
import org.hibernate.validator.group.GroupSequenceProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.kit.ValidationKit
import org.kuark.base.bean.validation.support.AbstractGroupSequenceProvider
import org.kuark.base.bean.validation.support.Group
import javax.validation.GroupSequence
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.groups.Default

/**
 * 分组校验测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class GroupValidationTest {

    /**
     * 测试过滤分组
     */
    @Test
    fun testFilterGroup() {
        // 仅Group.First::class分组
        val bean1 = TestFilterGroupBean(null, null)
        val violations1 = ValidationKit.validateBean(bean1)
        assertEquals("name不能为null", violations1.first().message)
        //// 有@GroupSequece，即使明确传入Default::class也无效（@GroupSequece优先级较高）
        val violations2 = ValidationKit.validateBean(bean1, Default::class)
        assertEquals("name不能为null", violations2.first().message)
    }

    /**
     * 测试同一分组内的约束的验证顺序：都是无序的
     */
    @Test
    fun testDefaultOrderSameGroupBean() {
        // 一个属性上的约束校验是无序的
        var msgSet = mutableSetOf<String>()
        for (i in 1 until 100) {
            val bean = TestValidateOrderSameGroupBean("123", "456", 61)
            val violations = ValidationKit.validateProperty(bean, "name", failFast = false)
            assertEquals(2, violations.size)
            msgSet.add(violations.first().message)
        }
        assertEquals(2, msgSet.size)

        // 跨属性的同一分组间的约束校验也是无序的
        msgSet.clear()
        for (i in 1 until 100) {
            val bean = TestValidateOrderSameGroupBean("123", "456", 61)
            val violations = ValidationKit.validateBean(bean, Group.First::class, failFast = false)
            assertEquals(3, violations.size)
            msgSet.add(violations.first().message)
        }
        assertEquals(3, msgSet.size)
    }

    /**
     * 测试分组顺序
     */
    @Test
    fun testGroupSequence() {
        for (i in 1 until 10) {
            val bean1 = TestGroupSequenceBean("123", 61)
            val violations1 = ValidationKit.validateBean(bean1, failFast = false) // 非快速失败模式会被无视
            assertEquals(1, violations1.size) // Group.First分组校验失败就中止了(分组顺序的短路效果)
            assertEquals("必须60岁以下", violations1.first().message)
        }
    }

    /**
     * 测试分组顺序提供者
     */
    @Test
    fun testGroupSequenceProvider() {
        for (i in 1 until 10) {
            // 先Group.First分组，后Group.Second分组
            val bean1 = TestGroupSequenceProviderBean("123", 61)
            val violations1 = ValidationKit.validateBean(bean1, failFast = false)
            assertEquals(1, violations1.size)
            assertEquals("必须60岁以下", violations1.first().message)

            // 仅Group.Second分组
            val bean2 = TestGroupSequenceProviderBean("456", 61)
            val violations2 = ValidationKit.validateBean(bean2, failFast = false)
            assertEquals(2, violations2.size)
        }
    }

    @GroupSequence(Group.First::class, TestFilterGroupBean::class) //!!! 必须要有当前类
    internal data class TestFilterGroupBean(

        @get:NotNull(message = "name不能为null", groups = [Group.First::class])
        val name: String?,

        @get:NotNull(message = "age不能为null") // group缺少为javax.validation.groups.Default::class
        val age: Int?
    )

    internal data class TestValidateOrderSameGroupBean(

        @get:Length(min = 6, max = 32, message = "name长度必须在6到32之间")
        @get:Pattern(regexp = "[a-zA-Z]+", message = "name必须为字母")
        val name: String?,

        @get:Length(min = 6, max = 32, message = "name长度必须在6到32之间", groups = [Group.First::class])
        @get:Pattern(regexp = "[a-zA-Z]+", message = "name必须为字母", groups = [Group.First::class])
        val name2: String?,

        @get:Max(60, message = "必须60岁以下", groups = [Group.First::class])
        @get:Min(18, message = "必须满18岁", groups = [Group.First::class])
        val age: Int?
    )

    @GroupSequence(Group.First::class, Group.Second::class, TestGroupSequenceBean::class) //!!! 必须要有当前类
    internal data class TestGroupSequenceBean(

        @get:Length(min = 6, max = 32, message = "name长度必须在6到32之间", groups = [Group.Second::class])
        @get:Pattern(regexp = "[a-zA-Z]+", message = "name必须为字母", groups = [Group.Second::class])
        val name: String?,

        @get:Max(60, message = "必须60岁以下", groups = [Group.First::class])
        @get:Min(18, message = "必须满18岁", groups = [Group.First::class])
        val age: Int?
    )

    @GroupSequenceProvider(TestGroupSequenceProviderBean.GroupSequenceProvider::class)
    internal data class TestGroupSequenceProviderBean(

        @get:Length(min = 6, max = 32, message = "name长度必须在6到32之间", groups = [Group.Second::class])
        @get:Pattern(regexp = "[a-zA-Z]+", message = "name必须为字母", groups = [Group.Second::class])
        val name: String?,

        @get:Max(60, message = "必须60岁以下", groups = [Group.First::class])
        @get:Min(18, message = "必须满18岁", groups = [Group.First::class])
        val age: Int?
    ) {
        class GroupSequenceProvider : AbstractGroupSequenceProvider<TestGroupSequenceProviderBean>() {

            override fun getGroups(bean: TestGroupSequenceProviderBean?): List<Class<*>> {
                val defaultGroupSequence = mutableListOf<Class<*>>()
                defaultGroupSequence.add(TestGroupSequenceProviderBean::class.java) // 必须添加Bean类自己，否则Default分组都不会执行了，会抛错

                if (bean != null) { // 这块判空请务必要做
                    if (bean.name == "123") {
                        defaultGroupSequence.add(Group.First::class.java)
                        defaultGroupSequence.add(Group.Second::class.java)
                    } else {
                        defaultGroupSequence.add(Group.Second::class.java)
                    }
                }
                return defaultGroupSequence
            }

        }
    }

}