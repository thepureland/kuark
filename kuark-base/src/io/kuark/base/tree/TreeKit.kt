package io.kuark.base.tree

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
     * @param <T>
     * @param <E>
     * @param objectList 结点对象列表
     * @return List<树根结点>
     * @since 1.0.0
     */
    fun <T, E : ITreeable<T>> convertListToTree(objectList: List<E>): List<TreeNode<E>> {
        return ListToTreeConverter.convert(objectList)
    }

    /**
     * 深度遍历树，并执行回调
     * @param nodes 树结点列表
     * @param callback 回调
     * @param <T>
     * @param <R>
     * @since 1.0.0
     */
    fun <T, R> depthTraverse(nodes: List<TreeNode<T>>, callback: ICallback<TreeNode<T>, R>) {
        for (node in nodes) {
            depth(node, callback)
        }
    }

    private fun <T, R> depth(node: TreeNode<T>, callback: ICallback<TreeNode<T>, R>) {
        callback.execute(node)
        val children = node.getChildren()
        for (subNode in children) {
            depth(subNode, callback)
        }
    }

}