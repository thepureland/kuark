package org.kuark.base.validation

import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import org.hibernate.validator.group.GroupSequenceProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kuark.base.validation.kit.ValidationKit
import org.kuark.base.validation.support.Group
import javax.validation.GroupSequence
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.groups.Default

internal class GroupTest {

    /**
     * 测试过滤分组
     */
//    @Test
//    fun testFilterGroup() {
//        // 仅Group.First::class分组
//        val bean1 = TestFilterGroupBean(null, null)
//        val violations1 = ValidationKit.validate(bean1)
//        assertEquals("name不能为null", violations1.first().message)
//        //// 有@GroupSequece，即使明确传入Default::class也无效（@GroupSequece优先级较高）
//        val violations2 = ValidationKit.validate(bean1, Default::class)
//        assertEquals("name不能为null", violations2.first().message)
//    }

    /**
     * 测试分组顺序
     */
    @Test
    fun testGroupSequence() {
        val bean1 = TestGroupSequenceBean("123", 61)
        val violations1 = ValidationKit.validate(bean1, failFast = false) // 非快速失败模式
//        assertEquals("name必须为字母", violations1.first().message)
        violations1.forEach { println(it.message) }
    }

    /**
     * 测试分组顺序提供者
     */
    @Test
    fun testGroupSequenceProvider() {

    }

//    @GroupSequence(Group.First::class, TestFilterGroupBean::class) //!!! 必须要有当前类
//    internal data class TestFilterGroupBean(
//
//        @get:NotNull(message = "name不能为null", groups = [Group.First::class])
//        val name: String?,
//
//        @get:NotNull(message = "age不能为null") // group缺少为javax.validation.groups.Default::class
//        val age: Int?
//    )


    @GroupSequence(Group.First::class, Group.Second::class, TestGroupSequenceBean::class) //!!! 必须要有当前类
    internal data class TestGroupSequenceBean(

        @get:Length(min = 6, max = 32, message = "name长度必须在6到32之间", groups = [Group.First::class])
        @get:Pattern(regexp = "[a-zA-Z]+", message = "name必须为字母", groups = [Group.First::class])
        val name: String?,

        @get:Max(60, message = "必须60岁以下", groups = [Group.Second::class])
        @get:Min(18, message = "必须满18岁", groups = [Group.Second::class])
        val age: Int?
    )


}