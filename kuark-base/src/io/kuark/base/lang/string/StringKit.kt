package io.kuark.base.lang.string

/**
 * 字符串操作工具类
 *
 * @author K
 * @since 1.0.0
 */
object StringKit {

    /**
     * 检测字符串是否为null或空串或为空白字符
     *
     * @param str 待检测字符串
     * @return true：字符串为null或空串或为空白字符，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isBlank(str: String?): Boolean = str == null || str.isBlank()

    /**
     * 检测字符串是否为null或空串
     *
     * @param str 待检测字符串
     * @return true：字符串为null或空串，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isEmpty(str: String?): Boolean = str == null || str.isEmpty()

    /**
     * 检测字符串是否不为：null、空串、空白字符
     *
     * @param str 待检测字符串
     * @return true：字符串不为：null、空串、空白字符，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isNotBlank(str: String?): Boolean = str != null && str.isNotBlank()

    /**
     * 检测字符串是否不为：null、空串
     *
     * @param str 待检测字符串
     * @return true：字符串不为：null、空串，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isNotEmpty(str: String?): Boolean = str != null && str.isNotEmpty()

}