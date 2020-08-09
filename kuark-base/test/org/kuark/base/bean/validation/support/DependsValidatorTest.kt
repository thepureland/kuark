package org.kuark.base.bean.validation.support

import org.junit.jupiter.api.Test
import org.kuark.base.support.logic.AndOr
import org.kuark.base.support.logic.Operator

/**
 *
 */
internal class DependsValidatorTest {

    @Test
    fun testValidate() {
        // 单条件
        assert(DependsValidator.validate(arrayOf("name1"), arrayOf("name1"), arrayOf(Operator.EQ)))

        // 多条件"与"
        assert(
            DependsValidator.validate(
                arrayOf("name1", "name2"), arrayOf("name1", "NAME2"), arrayOf(Operator.EQ, Operator.IEQ)
            )
        )

        // 多条件"或"
        assert(
            DependsValidator.validate(
                arrayOf("name1", "name3"), arrayOf("name1", "NAME2"), arrayOf(Operator.EQ, Operator.IEQ), AndOr.OR
            )
        )

        // 含数组
        assert(DependsValidator.validate(arrayOf("name1"), arrayOf("[name2,name1,name3]"), arrayOf(Operator.IN)))
    }

}