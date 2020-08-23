package io.kuark.base.bean.validation.support

import io.kuark.base.support.logic.AndOr
import io.kuark.base.support.logic.LogicOperator
import org.junit.jupiter.api.Test

/**
 * DependsValidator测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class DependsValidatorTest {

    @Test
    fun testValidate() {
        // 单条件
        assert(DependsValidator.validate(arrayOf("name1"), arrayOf("name1"), arrayOf(LogicOperator.EQ)))

        // 多条件"与"
        assert(
            DependsValidator.validate(
                arrayOf("name1", "name2"), arrayOf("name1", "NAME2"), arrayOf(LogicOperator.EQ, LogicOperator.IEQ)
            )
        )

        // 多条件"或"
        assert(
            DependsValidator.validate(
                arrayOf("name1", "name3"), arrayOf("name1", "NAME2"), arrayOf(LogicOperator.EQ, LogicOperator.IEQ), AndOr.OR
            )
        )

        // 含数组
        assert(DependsValidator.validate(arrayOf("name1"), arrayOf("[name2,name1,name3]"), arrayOf(LogicOperator.IN)))
    }

}