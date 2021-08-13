package io.kuark.base.lang.collections

/**
 * 集合操作工具类
 *
 * @author K
 * @since 1.0.0
 */
object CollectionKit {

    /**
     * 检测集合是否为null或空
     *
     * @param collection 集合
     * @return true: 集合为null或空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isEmpty(collection: Collection<*>?): Boolean = collection == null || collection.isEmpty()

    /**
     * 检测集合是否不为null且不为空
     *
     * @param collection 集合
     * @return true: 集合不为null且不为空，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isNotEmpty(collection: Collection<*>?): Boolean = collection != null && collection.isNotEmpty()

}