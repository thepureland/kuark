package org.kuark.base.validation.support

/**
 * 数列类型
 *
 * @author admin
 * @time 9/19/15 6:53 PM
 */
enum class SeriesType {
    /**
     * 递增
     */
    INC,

    /**
     * 递减
     */
    DESC,

    /**
     * 递增或相等
     */
    INC_EQ,

    /**
     * 递减或相等
     */
    DESC_EQ,

    /**
     * 各不相等
     */
    DIFF
}