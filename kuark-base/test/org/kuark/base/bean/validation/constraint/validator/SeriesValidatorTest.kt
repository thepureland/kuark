package org.kuark.base.bean.validation.constraint.validator

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.constraint.annotaions.Series
import org.kuark.base.bean.validation.kit.ValidationKit
import org.kuark.base.bean.validation.support.SeriesType
import java.math.BigDecimal
import java.math.BigInteger

/**
 * 数列验证器测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class SeriesValidatorTest {

    @Test
    fun validateIncDiff() {
        // Int型，递增，互不相等 -> pass
        assert(ValidationKit.validateValue(TestSeriesBean::class, "intIncDiff", arrayOf(1, 2, 5, 9, 11)).isEmpty())

        // Int型，递增，互不相等 -> fail（存在相等的情况）
        assertFalse(ValidationKit.validateValue(TestSeriesBean::class, "intIncDiff", arrayOf(1, 2, 2, 9, 11)).isEmpty())

        // Int型，递增，互不相等 -> fail（存在递减的情况）
        assertFalse(ValidationKit.validateValue(TestSeriesBean::class, "intIncDiff", arrayOf(1, 2, 1, 9, 11)).isEmpty())

        // Int型，递增，互不相等，有步进 -> pass
        assert(ValidationKit.validateValue(TestSeriesBean::class, "intIncDiffStep", arrayOf(1, 2, 3, 4, 5)).isEmpty())

        // Int型，递增，互不相等，有步进 -> fail（存在递减的情况）
        assertFalse(
            ValidationKit.validateValue(TestSeriesBean::class, "intIncDiffStep", arrayOf(1, 2, 1, 4, 5)).isEmpty()
        )

        // Int型，递增，互不相等，有步进 -> fail（违反步进值）
        assertFalse(
            ValidationKit.validateValue(TestSeriesBean::class, "intIncDiffStep", arrayOf(1, 2, 3, 4, 6)).isEmpty()
        )


        // Float型，递减，互不相等 -> pass
        assert(
            ValidationKit.validateValue(TestSeriesBean::class, "floatDescDiff", arrayOf(11F, 9F, 5F, 2F, 1F)).isEmpty()
        )

        // Float型，递减，互不相等 -> fail（存在相等的情况）
        assertFalse(
            ValidationKit.validateValue(TestSeriesBean::class, "floatDescDiff", arrayOf(11F, 9F, 2F, 2F, 1F)).isEmpty()
        )

        // Float型，递减，互不相等，有步进 -> pass
        assert(
            ValidationKit.validateValue(TestSeriesBean::class, "floatDescDiffStep", arrayOf(5F, 4F, 3F, 2F, 1F))
                .isEmpty()
        )

        // Float型，递减，互不相等，有步进 -> fail（违反步进值）
        assertFalse(
            ValidationKit.validateValue(TestSeriesBean::class, "floatDescDiffStep", arrayOf(6F, 4F, 3F, 2F, 1F))
                .isEmpty()
        )


        // Long型，先递增后递减，互不相等 -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "longIncDiffDescDiff", arrayOf(1L, 2L, 5L, 9L, 11L, 10L, 7L)
            ).isEmpty()
        )

        // Long型，先递增后递减，互不相等 -> fail（存在相等的情况）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "longIncDiffDescDiff", arrayOf(1L, 2L, 5L, 9L, 11L, 11L, 7L)
            ).isEmpty()
        )

        // Long型，先递增后递减，互不相等 -> fail（不只一次的递增或递减）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "longIncDiffDescDiff", arrayOf(1L, 2L, 10L, 9L, 11L, 10L, 7L)
            ).isEmpty()
        )

        // Long型，先递增后递减，互不相等，有步进 -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "longIncDiffDescDiffStep", arrayOf(1L, 2L, 3L, 4L, 5L, 4L, 3L)
            ).isEmpty()
        )

        // Long型，先递增后递减，互不相等，有步进 -> fail（不只一次的递增或递减）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "longIncDiffDescDiffStep", arrayOf(1L, 2L, 3L, 2L, 3L, 4L, 3L)
            ).isEmpty()
        )

        // Long型，先递增后递减，互不相等，有步进 -> fail（违反步进值）
        assertFalse(
            ValidationKit.validateValue(TestSeriesBean::class, "longIncDiffDescDiffStep", arrayOf(1L, 2L, 3L, 4L, 6L))
                .isEmpty()
        )


        // BigInteger型，先递减后递增，互不相等 -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "bigIntDescDiffIncDiff",
                arrayOf(BigInteger.valueOf(3), BigInteger.valueOf(2), BigInteger.valueOf(1), BigInteger.valueOf(5))
            ).isEmpty()
        )

        // BigInteger型，先递减后递增，互不相等 -> fail（存在相等的情况）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "bigIntDescDiffIncDiff",
                arrayOf(BigInteger.valueOf(3), BigInteger.valueOf(2), BigInteger.valueOf(2), BigInteger.valueOf(5))
            ).isEmpty()
        )

        // BigInteger型，先递减后递增，互不相等 -> fail（不只一次的递增或递减）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "bigIntDescDiffIncDiff",
                arrayOf(BigInteger.valueOf(3), BigInteger.valueOf(2), BigInteger.valueOf(4), BigInteger.valueOf(3))
            ).isEmpty()
        )

        // BigInteger型，先递减后递增，互不相等，有步进 -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "bigIntDescDiffIncDiffStep",
                arrayOf(BigInteger.valueOf(3), BigInteger.valueOf(2), BigInteger.valueOf(1), BigInteger.valueOf(2))
            ).isEmpty()
        )

        // BigInteger型，先递减后递增，互不相等，有步进 -> fail（不只一次的递增或递减）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "bigIntDescDiffIncDiffStep",
                arrayOf(BigInteger.valueOf(3), BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(2))
            ).isEmpty()
        )

        // BigInteger型，先递减后递增，互不相等，有步进 -> fail（违反步进值）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "bigIntDescDiffIncDiffStep",
                arrayOf(BigInteger.valueOf(3), BigInteger.valueOf(1), BigInteger.valueOf(3), BigInteger.valueOf(5))
            ).isEmpty()
        )


        // BigDecimal型，互不相等 -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "bigDecimalDiff",
                arrayOf(BigDecimal(1), BigDecimal(5), BigDecimal(3), BigDecimal(7))
            ).isEmpty()
        )

        // BigDecimal型，互不相等 -> fail（存在相等的情况）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "bigDecimalDiff",
                arrayOf(BigDecimal(1), BigDecimal(5), BigDecimal(5), BigDecimal(7))
            ).isEmpty()
        )

        // Int型，互不相等，有步进 -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "bigDecimalDiffStep",
                arrayOf(BigDecimal(1), BigDecimal(2), BigDecimal(3), BigDecimal(4))
            ).isEmpty()
        )

        // Int型，互不相等，有步进 -> fail（违反步进值）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "bigDecimalDiffStep",
                arrayOf(BigDecimal(1), BigDecimal(2), BigDecimal(3), BigDecimal(5))
            ).isEmpty()
        )


        // Double型，递增，可相等 -> pass
        assert(
            ValidationKit.validateValue(TestSeriesBean::class, "doubleIncEq", arrayOf(1.0, 2.0, 5.0, 9.0, 11.0))
                .isEmpty()
        )

        // Double型，递增，可相等 -> pass
        assert(
            ValidationKit.validateValue(TestSeriesBean::class, "doubleIncEq", arrayOf(1.0, 2.0, 2.0, 9.0, 11.0))
                .isEmpty()
        )

        // Double型，递增，可相等 -> fail (存在递减的情况)
        assertFalse(
            ValidationKit.validateValue(TestSeriesBean::class, "doubleIncEq", arrayOf(1.0, 2.0, 1.0, 9.0, 11.0))
                .isEmpty()
        )

        // Double型，递增，可相等，有步进 -> pass
        assert(
            ValidationKit.validateValue(TestSeriesBean::class, "doubleIncEqStep", arrayOf(1.0, 2.0, 3.0, 4.0, 5.0))
                .isEmpty()
        )

        // Double型，递增，可相等，有步进 -> fail (存在递减的情况)
        assertFalse(
            ValidationKit.validateValue(TestSeriesBean::class, "doubleIncEqStep", arrayOf(1.0, 2.0, 1.0, 3.0, 4.0, 5.0))
                .isEmpty()
        )

        // Double型，递增，可相等，有步进 -> fail（违反步进值）
        assertFalse(
            ValidationKit.validateValue(TestSeriesBean::class, "doubleIncEqStep", arrayOf(1.0, 2.0, 3.0, 4.0, 6.0))
                .isEmpty()
        )

        // String型，递减，可相等 -> pass
        assert(
            ValidationKit.validateValue(TestSeriesBean::class, "stringDescEq", arrayOf("11", "9", "5", "2", "1"))
                .isEmpty()
        )

        // String型，递减，可相等 -> pass
        assert(
            ValidationKit.validateValue(TestSeriesBean::class, "stringDescEq", arrayOf("11", "9", "2", "2", "1"))
                .isEmpty()
        )

        // String型，递减，可相等 -> fail（存在递增的情况）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class,
                "stringDescEq",
                arrayOf("11", "9", "5", "6", "1")
            ).isEmpty()
        )

        // String型，递减，可相等，有步进 -> pass
        assert(
            ValidationKit.validateValue(TestSeriesBean::class, "stringDescEqStep", arrayOf("5", "4", "3", "2", "1"))
                .isEmpty()
        )

        // String型，递减，可相等，有步进 -> pass
        assert(
            ValidationKit.validateValue(TestSeriesBean::class, "stringDescEqStep", arrayOf("5", "4", "3", "2", "2"))
                .isEmpty()
        )

        // String型，递减，可相等，有步进 -> fail（存在递增的情况）
        assertFalse(
            ValidationKit.validateValue(TestSeriesBean::class, "stringDescEqStep", arrayOf("5", "4", "3", "4", "1"))
                .isEmpty()
        )

        // String型，递减，可相等，有步进 -> fail（违反步进值）
        assertFalse(
            ValidationKit.validateValue(TestSeriesBean::class, "stringDescEqStep", arrayOf("6", "4", "3", "2", "1"))
                .isEmpty()
        )


        // Byte型，先递增(可相等)后递减(可相等) -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "byteIncDiffDescDiff",
                arrayOf(1.toByte(), 2.toByte(), 5.toByte(), 9.toByte(), 11.toByte(), 10.toByte(), 7.toByte())
            ).isEmpty()
        )

        // Byte型，先递增(可相等)后递减(可相等) -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "byteIncDiffDescDiff",
                arrayOf(1.toByte(), 2.toByte(), 5.toByte(), 9.toByte(), 11.toByte(), 11.toByte(), 7.toByte())
            ).isEmpty()
        )

        // Byte型，先递增(可相等)后递减(可相等) -> fail（不只一次的递增或递减）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "byteIncDiffDescDiff",
                arrayOf(1.toByte(), 2.toByte(), 10.toByte(), 9.toByte(), 11.toByte(), 10.toByte(), 7.toByte())
            ).isEmpty()
        )

        // Byte型，先递增(可相等)后递减(可相等)，有步进 -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "byteIncDiffDescDiffStep",
                arrayOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 4.toByte(), 3.toByte())
            ).isEmpty()
        )

        // Byte型，先递增(可相等)后递减(可相等)，有步进 -> fail（不只一次的递增或递减）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "byteIncDiffDescDiffStep",
                arrayOf(1.toByte(), 2.toByte(), 3.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 3.toByte())
            ).isEmpty()
        )

        // Byte型，先递增(可相等)后递减(可相等)，有步进 -> fail（违反步进值）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "byteIncDiffDescDiffStep",
                arrayOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 6.toByte())
            )
                .isEmpty()
        )


        // Short型，先递减(可相等)后递增(可相等) -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "shortDescDiffIncDiff",
                arrayOf(3.toShort(), 2.toShort(), 1.toShort(), 5.toShort())
            ).isEmpty()
        )

        // Short型，先递减(可相等)后递增(可相等) -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "shortDescDiffIncDiff",
                arrayOf(3.toShort(), 2.toShort(), 2.toShort(), 5.toShort())
            ).isEmpty()
        )

        // Short型，先递减(可相等)后递增(可相等) -> fail（不只一次的递增或递减）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "shortDescDiffIncDiff",
                arrayOf(3.toShort(), 2.toShort(), 4.toShort(), 3.toShort())
            ).isEmpty()
        )

        // Short型，先递减(可相等)后递增(可相等)，有步进 -> pass
        assert(
            ValidationKit.validateValue(
                TestSeriesBean::class, "shortDescDiffIncDiffStep",
                arrayOf(3.toShort(), 2.toShort(), 1.toShort(), 2.toShort())
            ).isEmpty()
        )

        // Short型，先递减(可相等)后递增(可相等)，有步进 -> fail（不只一次的递增或递减）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "shortDescDiffIncDiffStep",
                arrayOf(3.toShort(), 2.toShort(), 3.toShort(), 2.toShort())
            ).isEmpty()
        )

        // Short型，先递减(可相等)后递增(可相等)，有步进 -> fail（违反步进值）
        assertFalse(
            ValidationKit.validateValue(
                TestSeriesBean::class, "shortDescDiffIncDiffStep",
                arrayOf(3.toShort(), 1.toShort(), 3.toShort(), 5.toShort())
            ).isEmpty()
        )


        // Int型，全相等 -> pass
        assert(ValidationKit.validateValue(TestSeriesBean::class, "intEq", arrayOf(1, 1, 1, 1)).isEmpty())

        // Int型，全相等 -> fail (存在不相等情况)
        assertFalse(ValidationKit.validateValue(TestSeriesBean::class, "intEq", arrayOf(1, 1, 2, 1)).isEmpty())

        // Int型，全相等 -> fail (数列大小不匹配)
        assertFalse(ValidationKit.validateValue(TestSeriesBean::class, "intEq", arrayOf(1, 1, 1, 1, 1)).isEmpty())
    }

    internal data class TestSeriesBean(

        @get:Series(type = SeriesType.INC_DIFF, message = "必须递增且互不相等")
        val intIncDiff: Array<Int>,

        @get:Series(type = SeriesType.INC_DIFF, step = 1.0, message = "必须递增且互不相等，且步进为1")
        val intIncDiffStep: Array<Int>,

        @get:Series(type = SeriesType.DESC_DIFF, message = "必须递减且互不相等")
        val floatDescDiff: Array<Float>,

        @get:Series(type = SeriesType.DESC_DIFF, step = 1.0, message = "必须递减且互不相等，且步进为1")
        val floatDescDiffStep: Array<Float>,

        @get:Series(type = SeriesType.INC_DIFF_DESC_DIFF, message = "必须先递增后递减且互不相等")
        val longIncDiffDescDiff: Array<Long>,

        @get:Series(type = SeriesType.INC_DIFF_DESC_DIFF, step = 1.0, message = "必须先递增后递减且互不相等，且步进为1")
        val longIncDiffDescDiffStep: Array<Long>,

        @get:Series(type = SeriesType.DESC_DIFF_INC_DIFF, message = "必须先递减后递增且互不相等")
        val bigIntDescDiffIncDiff: Array<BigInteger>,

        @get:Series(type = SeriesType.DESC_DIFF_INC_DIFF, step = 1.0, message = "必须先递减后递增且互不相等，且步进为1")
        val bigIntDescDiffIncDiffStep: Array<BigInteger>,

        @get:Series(type = SeriesType.DIFF, message = "必须互不相等")
        val bigDecimalDiff: Array<BigDecimal>,

        @get:Series(type = SeriesType.DIFF, step = 1.0, message = "必须互不相等，且步进为1")
        val bigDecimalDiffStep: Array<BigDecimal>,


        @get:Series(type = SeriesType.INC_EQ, message = "必须递增(可相等)")
        val doubleIncEq: Array<Double>,

        @get:Series(type = SeriesType.INC_EQ, step = 1.0, message = "必须递增(可相等)，且不等时步进为1")
        val doubleIncEqStep: Array<Double>,

        @get:Series(type = SeriesType.DESC_EQ, message = "必须递减(可相等)")
        val stringDescEq: Array<String>,

        @get:Series(type = SeriesType.DESC_EQ, step = 1.0, message = "必须递减(可相等)，且不等时步进为1")
        val stringDescEqStep: Array<String>,

        @get:Series(type = SeriesType.INC_EQ_DESC_EQ, message = "必须先递增(可相等)后递减(可相等)")
        val byteIncDiffDescDiff: Array<Byte>,

        @get:Series(type = SeriesType.INC_EQ_DESC_EQ, step = 1.0, message = "必须先递增(可相等)后递减(可相等)，且不等时步进为1")
        val byteIncDiffDescDiffStep: Array<Byte>,

        @get:Series(type = SeriesType.DESC_EQ_INC_EQ, message = "必须先递减(可相等)后递增(可相等)")
        val shortDescDiffIncDiff: Array<Short>,

        @get:Series(type = SeriesType.DESC_EQ_INC_EQ, step = 1.0, message = "必须先递减(可相等)后递增(可相等)，且不等时步进为1")
        val shortDescDiffIncDiffStep: Array<Short>,

        @get:Series(type = SeriesType.EQ, size=4, message = "必须全相等，且数列大小为4")
        val intEq: List<Int>

    ) {
        override fun equals(other: Any?): Boolean = super.equals(other)
        override fun hashCode(): Int = super.hashCode()
    }

}