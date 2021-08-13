package io.kuark.base.lang.collections


/**
 * 数组操作工具类
 *
 * @author K
 * @since 1.0.0
 */
object ArrayKit {

    /**
     * 检测数组是否为null或空
     *
     * @param array 数组
     * @return true: 数组为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isEmpty(array: Array<*>?): Boolean = array == null || array.isEmpty()

    /**
     * 检测字节数组是否为null或空
     *
     * @param array 字节数组
     * @return true: 字节数组为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isByteArrayEmpty(array: ByteArray?): Boolean = array == null || array.isEmpty()

    /**
     * 检测字符数组是否为null或空
     *
     * @param array 字符数组
     * @return true: 字符数组为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isCharArrayEmpty(array: CharArray?): Boolean = array == null || array.isEmpty()

    /**
     * 检测Short数组是否为null或空
     *
     * @param array Short数组
     * @return true: Short数组为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isShortArrayEmpty(array: ShortArray?): Boolean = array == null || array.isEmpty()

    /**
     * 检测Int数组是否为null或空
     *
     * @param array Int数组
     * @return true: Int数组为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isIntArrayEmpty(array: IntArray?): Boolean = array == null || array.isEmpty()

    /**
     * 检测Long数组是否为null或空
     *
     * @param array Long数组
     * @return true: Long数组为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isLongArrayEmpty(array: LongArray?): Boolean = array == null || array.isEmpty()

    /**
     * 检测Float数组是否为null或空
     *
     * @param array Float数组
     * @return true: Float数组为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isFloatArrayEmpty(array: FloatArray?): Boolean = array == null || array.isEmpty()

    /**
     * 检测Double数组是否为null或空
     *
     * @param array Double数组
     * @return true: Double数组为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isDoubleArrayEmpty(array: DoubleArray?): Boolean = array == null || array.isEmpty()

    /**
     * 检测布尔数组是否为null或空
     *
     * @param array 布尔数组
     * @return true: 布尔数组为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isBooleanArrayEmpty(array: BooleanArray?): Boolean = array == null || array.isEmpty()

    /**
     * 检测数组是否不为null且不为空
     *
     * @param array 数组
     * @return true: 数组不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isNotEmpty(array: Array<*>?): Boolean = array != null && array.isNotEmpty()

    /**
     * 检测字节数组是否不为null且不为空
     *
     * @param array 字节数组
     * @return true: 字节数组不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isByteArrayNotEmpty(array: ByteArray?): Boolean = array != null && array.isNotEmpty()

    /**
     * 检测字符数组是否不为null且不为空
     *
     * @param array 字符数组
     * @return true: 字符数组不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isCharArrayNotEmpty(array: CharArray?): Boolean = array != null && array.isNotEmpty()

    /**
     * 检测Short数组是否不为null且不为空
     *
     * @param array Short数组
     * @return true: Short数组不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isShortArrayNotEmpty(array: ShortArray?): Boolean = array != null && array.isNotEmpty()

    /**
     * 检测Int数组是否不为null且不为空
     *
     * @param array Int数组
     * @return true: Int数组不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isIntArrayNotEmpty(array: IntArray?): Boolean = array != null && array.isNotEmpty()

    /**
     * 检测Long数组是否不为null且不为空
     *
     * @param array Long数组
     * @return true: Long数组不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isLongArrayNotEmpty(array: LongArray?): Boolean = array != null && array.isNotEmpty()

    /**
     * 检测Float数组是否不为null且不为空
     *
     * @param array Float数组
     * @return true: Float数组不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isFloatArrayNotEmpty(array: FloatArray?): Boolean = array != null && array.isNotEmpty()

    /**
     * 检测Double数组是否不为null且不为空
     *
     * @param array Double数组
     * @return true: Double数组不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isDoubleArrayNotEmpty(array: DoubleArray?): Boolean = array != null && array.isNotEmpty()

    /**
     * 检测布尔数组是否不为null且不为空
     *
     * @param array 布尔数组
     * @return true: 布尔数组不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isBooleanArrayNotEmpty(array: BooleanArray?): Boolean = array != null && array.isNotEmpty()

}