package org.kuark.base.bean.validation.teminal

import org.hibernate.validator.constraints.Length
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.constraint.annotaions.AtLeast
import org.kuark.base.bean.validation.constraint.annotaions.Compare
import org.kuark.base.bean.validation.constraint.annotaions.DictEnumCode
import org.kuark.base.bean.validation.constraint.annotaions.Series
import org.kuark.base.bean.validation.support.Depends
import org.kuark.base.bean.validation.support.RegExps
import org.kuark.base.bean.validation.support.SeriesType
import org.kuark.base.support.enums.Sex
import org.kuark.base.support.logic.CompareLogic
import javax.validation.Valid
import javax.validation.constraints.*

internal class TeminalConstraintsCreatorTest {

    @Test
    fun create() {
        val result = TeminalConstraintsCreator.create(TestRegisterBean::class)
        println(result)
    }

    @AtLeast(properties = ["mobile", "email"], message = "必须至少提供一种联系方式")
    internal data class TestRegisterBean(

        @get:NotBlank
        val username: String?,

        @get:NotNull
        @get:Length(min = 8, max = 32, message = "密码长度必须在8到32间")
        val password: String?,

        @get:Compare(
            depends = Depends(
                properties = ["validate"],
                values = ["true"]
            ),
            anotherProperty = "password",
            logic = CompareLogic.EQ,
            message = "两次密码不同"
        )
        val confirmPassword: String?,

        @get:Pattern(regexp = RegExps.MOBILE, message = "手机号码格式错误")
        val mobile: String?,

        @get:Email
        val email: String?,

        @get:NotNull
        @get:Min(18, message = "未满18周岁不能注册")
        val age: Int?,

        @get:NotNull
        @get:DictEnumCode(enumClass = Sex::class, message = "性别错误")
        val sex: String,

        @get:NotNull
        @get:Series(type = SeriesType.INC_DIFF, step = 2.0, message = "机器人识别问题回答错误")
        val question: Array<Int>?,

        @Valid
        val address: Address

    ) {
        override fun equals(other: Any?): Boolean = true
        override fun hashCode(): Int = 0
    }

    internal data class Address(

        @get:NotNull
        val country: String?,

        @get:NotNull
        val province: String?,

        val city: String?

    )

}