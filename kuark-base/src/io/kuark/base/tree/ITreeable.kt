package io.kuark.base.tree

import java.io.Serializable

/**
 * 列表转为树结构的约束的接口
 *
 * @param T 结点标识类型
 * @author K
 * @since 1.0.0
 */
interface ITreeable<T> : Serializable {

    /**
     * 当前结点的惟一标识
     *
     * @author K
     * @since 1.0.0
     */
    val selfUniqueIdentifier: T

    /**
     * 父结点的惟一标识
     *
     * @author K
     * @since 1.0.0
     */
    val parentUniqueIdentifier: T

}