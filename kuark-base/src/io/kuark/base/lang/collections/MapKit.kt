package io.kuark.base.lang.collections


/**
 * Map操作工具类
 *
 * @author K
 * @since 1.0.0
 */
object MapKit {

    /**
     * 检测Map是否为null或空
     *
     * @param map Map
     * @return true: Map为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isEmpty(map: Map<*, *>?): Boolean = map == null || map.isEmpty()

    /**
     * 检测Map是否不为null且不为空
     *
     * @param map Map
     * @return true: Map不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isNotEmpty(map: Map<*, *>?): Boolean = map != null && map.isNotEmpty()

}