package io.kuark.base.tree

import io.kuark.base.lang.collections.CollectionKit
import io.kuark.base.query.sort.Direction
import io.kuark.base.support.ICallback

/**
 * 树操作工具类
 *
 * @author K
 * @since 1.0.0
 */
object TreeKit {

    /**
     * 将列表结构转为树结构
     *
     * @param T 树结点惟一标识的类型
     * @param E 树结点类型
     * @param treeNodeList 结点对象列表
     * @param direction 排序，指定排序时E必须实现Comparable接口，为null将不做排序，默认为null
     * @return List(树根结点)
     * @author K
     * @since 1.0.0
     */
    fun <T, E : ITreeNode<T>> convertListToTree(treeNodeList: List<E>, direction: Direction? = null): List<E> {
        return ListToTreeConverter.convert(treeNodeList, direction)
    }

    /**
     * 深度遍历树，并执行回调
     *
     * @param T 树结点惟一标识的类型
     * @param R 回调返回值类型
     * @param nodes 树结点列表
     * @param callback 回调
     * @author K
     * @since 1.0.0
     */
    fun <T, R> depthTraverse(nodes: List<ITreeNode<T>>, callback: ICallback<ITreeNode<T>, R>) {
        for (node in nodes) {
            depth(node, callback)
        }
    }

    private fun <T, R> depth(node: ITreeNode<T>, callback: ICallback<ITreeNode<T>, R>) {
        callback.execute(node)
        val children = node._getChildren()
        if (CollectionKit.isNotEmpty(children)) {
            for (subNode in children) {
                depth(subNode, callback)
            }
        }

    }

}