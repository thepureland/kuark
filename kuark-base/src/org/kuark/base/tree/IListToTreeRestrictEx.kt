package org.kuark.base.tree

/**
 * 扩展@see IListToTreeRestrict
 * 当业务ID已经被占用的情况下，可以使用此接口
 *
 * @since 1.0.0
 * @author admin
 * @time 2017-6-22 下午16:50:48
 */
interface IListToTreeRestrictEx<T> : IListToTreeRestrict<T> {
    /**
     * 获取当前结点的id
     *
     * @return 当前结点的id
     * @author admin
     * @time 2017-6-22 下午16:50:48
     */
    fun getSelfId(): T
}