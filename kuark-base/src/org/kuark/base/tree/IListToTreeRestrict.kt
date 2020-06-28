package org.kuark.base.tree

import java.io.Serializable

/**
 * 列表转为树结构的约束的接口
 *
 * @since 1.0.0
 * @author K
 */
interface IListToTreeRestrict<T> : Serializable {
    /**
     * 获取当前结点的id
     *
     * @return 当前结点的id
     */
    val id: T?

    /**
     * 获取父结点的id
     *
     * @return 父结点的id
     */
    val parentId: T?
}