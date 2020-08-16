package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.constraint.annotaions.Compare
import org.kuark.base.bean.validation.support.Depends
import org.kuark.base.bean.validation.teminal.convert.ConstraintConvertContext
import org.kuark.base.support.logic.LogicOperator
import kotlin.reflect.full.memberProperties

internal class CompareConstraintConvertorTest {

    @Test
    fun test() {
        val context = ConstraintConvertContext("confirmPassword", null, CompareTestBean::class)
        val prop = CompareTestBean::class.memberProperties.first { it.name == "confirmPassword" }
        val annotation = prop.getter.annotations.first()
        val teminalConstraint = CompareConstraintConvertor(annotation).convert(context)
        println(teminalConstraint)

        val context1 = ConstraintConvertContext("medium", null, CompareTestBean::class)
        val prop1 = CompareTestBean::class.memberProperties.first { it.name == "medium" }
        val annotation1 = prop1.getter.annotations.first()
        val teminalConstraint1 = CompareConstraintConvertor(annotation1).convert(context1)
        println(teminalConstraint1)

    }

    internal data class CompareTestBean(
        val validate: Boolean?,

        val password: String?,

        @get:Compare(
            depends = Depends(
                properties = ["validate"],
                values = ["true"]
            ),
            anotherProperty = "password",
            logic = LogicOperator.EQ,
            message = "两次密码不同"
        )
        val confirmPassword: String?,


        @get:Compare.List(
            Compare(
                anotherProperty = "small",
                logic = LogicOperator.GT,
                message = "medium必须大于small"
            ),
            Compare(
                anotherProperty = "large",
                logic = LogicOperator.LT,
                message = "medium必须小于large"
            )
        )
        val medium: String?
    )

}