package org.kuark.base.validation.constraint.validator

import org.hibernate.validator.constraints.Length
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kuark.base.validation.constraint.annotaions.Depends
import org.kuark.base.validation.kit.ValidationKit
import org.kuark.base.validation.support.Group
import org.kuark.base.validation.support.Operator
import javax.validation.GroupSequence
import javax.validation.constraints.NotNull
import javax.validation.groups.Default

internal class DependsValidatorTest {

    @Test
    fun testValidate() {
        // 当前属性值为null，并且没有@NotNull约束，无须校验，全部通过
        val bean1 = DependsTestBean1(false, null)
        assert(ValidationKit.validate(bean1).isEmpty())

        // 依赖条件不成立时，不用继续校验该属性上的其它约束
        val bean2 = DependsTestBean1(false, "")
        assert(ValidationKit.validate(bean2).isEmpty())

        // 依赖条件成立且当前属性值不为null时，继续校验该属性上的其它约束
        val bean3 = DependsTestBean1(true, "")
        val violations = ValidationKit.validate(bean3)
        assert(violations.isNotEmpty())
        assertEquals("name长度必须在6到32之间", violations.first().message)
    }

    @GroupSequence(Group.First::class, Group.Second::class, DependsTestBean1::class)
    data class DependsTestBean1(
        @get:NotNull // 该约束因为group=Default::class，并未生效
        val validate: Boolean,

//        @get:Depends(property = ["validate"], operator = [Operator.EQ], value = ["true"], groups = [Group.First::class])
        @get:Length(min = 6, max = 32, message = "name长度必须在6到32之间", groups = [Group.Second::class])
        val name: String?
    )

}