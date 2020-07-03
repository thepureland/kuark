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
     * 当前结点的惟一标识
     */
    val selfUniqueIdentifier: T

    /**
     * 父结点的惟一标识
     */
    val parentUniqueIdentifier: T

}