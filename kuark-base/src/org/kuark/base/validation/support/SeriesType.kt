package org.kuark.base.validation.support

/**
 * 数列类型
 *
 * @author K
 * @since 1.0.0
 */
enum class SeriesType {

    /** 递增且互不相等 */
    INC_DIFF,

    /** 递减且互不相等 */
    DESC_DIFF,

    /** 先增后减且互不相等 */
    INC_DESC_DIFF,

    /** 先减后增且互不相等 */
    DESC_INC_DIFF,

    /** 互不相等 */
    DIFF,

    /** 递增或相等 */
    INC_EQ,

    /** 递减或相等 */
    DESC_EQ,

    /** 先递增或相等，再递减或相等 */
    INC_EQ_DESC_EQ,

    /** 先递减或相等，再递增或相等 */
    DESC_EQ_INC_EQ,

    /** 全等 */
    EQ

}