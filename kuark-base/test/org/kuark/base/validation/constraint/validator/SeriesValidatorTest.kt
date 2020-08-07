package org.kuark.base.validation.constraint.validator

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.kuark.base.validation.constraint.annotaions.Series
import org.kuark.base.validation.kit.ValidationKit
import org.kuark.base.validation.support.SeriesType

internal class SeriesValidatorTest {

    @Test
    fun validateIncDiff() {
        assert(ValidationKit.validateValue(TestSeriesBean::class, "intIncDiff", arrayOf(1, 2, 5, 9, 11)).isEmpty())
        assertFalse(ValidationKit.validateValue(TestSeriesBean::class, "intIncDiff", arrayOf(1, 2, 2, 9, 11)).isEmpty())
        assert(ValidationKit.validateValue(TestSeriesBean::class, "intIncDiffStep", arrayOf(1, 2, 3, 4, 5)).isEmpty())
        assertFalse(ValidationKit.validateValue(TestSeriesBean::class, "intIncDiffStep", arrayOf(1, 2, 3, 4, 6)).isEmpty())

    }

    internal data class TestSeriesBean(

        @get:Series(type = SeriesType.INC_DIFF, message = "必须递增且互不相等")
        val intIncDiff: Array<Int>,

        @get:Series(type = SeriesType.INC_DIFF, step = 1.0, message = "必须递增且互不相等，且步进为1")
        val intIncDiffStep: Array<Int>

    ) {
        override fun equals(other: Any?): Boolean = super.equals(other)
        override fun hashCode(): Int = super.hashCode()
    }

}