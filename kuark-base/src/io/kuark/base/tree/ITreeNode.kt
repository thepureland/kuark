package io.kuark.base.tree

import java.beans.Transient
import java.io.Serializable

/**
 * 树结点接口
 *
 * @param T 结点标识类型
 * @author K
 * @since 1.0.0
 */
interface ITreeNode<T> : Serializable {

    /**
     * 返回当前结点的惟一标识
     *
     * @author K
     * @since 1.0.0
     */
    @Transient
    fun _getId(): T

    /**
     * 返回父结点的惟一标识
     *
     * @author K
     * @since 1.0.0
     */
    @Transient
    fun _getParentId(): T?

    /**
     * 返回孩子结点
     * 通过id和parentId其实就可以构造完整的树，该属性只是为了用户使用方便引入。
     * 用户可视自己需要重写该方法，并返回其对应的属性值。
     * 如果使用TreeKit::convertListToTree()，会自动填充孩子结点，无须用户自己维护父子关系。
     *
     * @return 孩子结点列表
     * @author K
     * @since 1.0.0
     */
    @Transient
    fun _getChildren(): MutableList<ITreeNode<T>> = mutableListOf()

}