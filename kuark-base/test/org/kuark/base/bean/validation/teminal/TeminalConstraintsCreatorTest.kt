package org.kuark.base.bean.validation.teminal

import org.hibernate.validator.constraints.*
import org.hibernate.validator.constraints.time.DurationMax
import org.hibernate.validator.constraints.time.DurationMin
import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.constraint.annotaions.AtLeast
import org.kuark.base.bean.validation.constraint.annotaions.Compare
import org.kuark.base.bean.validation.constraint.annotaions.DictEnumCode
import org.kuark.base.bean.validation.constraint.annotaions.Series
import org.kuark.base.bean.validation.support.Depends
import org.kuark.base.bean.validation.support.RegExps
import org.kuark.base.bean.validation.support.SeriesType
import org.kuark.base.support.enums.Sex
import org.kuark.base.support.logic.LogicOperator
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

internal class TeminalConstraintsCreatorTest {

    @Test
    fun create() {
        val result = TeminalConstraintsCreator.create(TestRegisterBean::class)
        println(result)
    }

    @AtLeast(properties = ["mobile", "email"], message = "必须至少提供一种联系方式")
//    @ScriptAssert(lang = "javascript", script = "1==1") //kuark暂不支持
    internal data class TestRegisterBean(

        @get:AssertTrue
        val validate: Boolean?,

        @get:AssertFalse
        val guest: Boolean?,

        @get:Null
        val error: String?,

        @get:NotBlank
        val username: String?,

        @get:NotNull
        @get:Length(min = 8, max = 32, message = "密码长度必须在8到32间")
        val password: String?,

        @get:Compare.List(
            Compare(
                depends = Depends(
                    properties = ["validate"],
                    values = ["true"]
                ),
                anotherProperty = "password",
                logic = LogicOperator.EQ,
                message = "两次密码不同"
            ),
            Compare(
                anotherProperty = "username",
                logic = LogicOperator.IN,
                message = "密码不能包含用户名"
            )

        )
        val confirmPassword: String?,

        @get:Pattern(regexp = RegExps.MOBILE, message = "手机号码格式错误")
        val mobile: String?,

//        @get:Email  //统一用Pattern代替
        @get:Pattern(regexp = RegExps.EMAIL, message = "电子邮箱格式错误")
        val email: String?,

        @get:Min(18, message = "未满18周岁不能注册")
        @get:Max(60, message = "超过60周岁不能注册")
        val age: Int?,

        @get:Past
        val greduateDate: LocalDate?,

        @get:Future
        val expireDate: LocalDate?,

        @get:PastOrPresent
        val date1: LocalDate?,

        @get:FutureOrPresent
        val date2: LocalDate?,

        @get:DurationMax
        val time1: LocalDateTime?,

        @get:DurationMin
        val time2: LocalDateTime?,

        @get:DecimalMin("50.0", message = "体重必须大于50.0KG")
        @get:DecimalMax("100.0", message = "体重必须小于100.0KG")
        val weigth: Double?,

        @get:Range(min=30, max = 270, message = "身高值必须在30cm到270cm之间")
        val height: Double?,

        @get:Positive(message = "视力必须为正数")
        @get:Digits(integer = 1, fraction = 1, message = "视力值必须是1位整数和1位小数组成")
        val eyesight: Double?,

        @get:Negative
        val value1: Double?,

        @get:NegativeOrZero
        val value2: Double?,

        @get:PositiveOrZero
        val value3: Double?,

        @get:CreditCardNumber
        val creditCardNumber: String?,

        @get:Currency
        val currency: String?,

        @get:EAN
        val barcode: String?,

        @get:LuhnCheck
        val string1: String?,

        @get:Mod10Check
        val string2: String?,

        @get:Mod11Check
        val string3: String?,

        @get:SafeHtml
        val richText: String?,

//        @get:URL  //统一用Pattern代替
        val photo: String?,

        @get:Size(min = 3, max = 6, message = "业余爱好必须选3到6项")
        val hobbies: Array<String>?,

        @get:DictEnumCode(enumClass = Sex::class, message = "性别错误")
        val sex: String,

        @get:NotEmpty
        @get:Series(type = SeriesType.INC_DIFF, step = 2.0, message = "机器人识别问题回答错误")
        val question: Array<Int>?,

        @get:Valid
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