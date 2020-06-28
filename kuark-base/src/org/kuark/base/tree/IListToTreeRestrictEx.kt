package org.kuark.base.tree

/**
 * 扩展@see IListToTreeRestrict
 * 当业务ID已经被占用的情况下，可以使用此接口
 *
 * @since 1.0.0
 * @author K
 */
interface IListToTreeRestrictEx<T> : IListToTreeRestrict<T> {
    /**
     * 获取当前结点的id
     *
     * @return 当前结点的id
     */
    fun getSelfId(): T
}